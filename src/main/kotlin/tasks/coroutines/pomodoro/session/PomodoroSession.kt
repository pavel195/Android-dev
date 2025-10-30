package tasks.coroutines.pomodoro.session

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import tasks.coroutines.pomodoro.PomodoroConfig
import tasks.coroutines.pomodoro.PomodoroState
import tasks.coroutines.pomodoro.UiSnapshot

sealed class SessionCommand {
    data object Start : SessionCommand()
    data object Pause : SessionCommand()
    data object Stop : SessionCommand()
    data class Adjust(val workMinutes: Int? = null, val restMinutes: Int? = null) : SessionCommand()
}

class PomodoroSession(
    val id: Int,
    val name: String,
    initialConfig: PomodoroConfig,
    private val scope: CoroutineScope
) {
    private val commands = Channel<SessionCommand>(Channel.UNLIMITED)
    private val mutex = Mutex()
    private var config: PomodoroConfig = initialConfig
    private var worker: Job? = null

    private val _snapshot = MutableStateFlow(
        UiSnapshot(id, name, PomodoroState.Stopped, secondsLeft = config.workMinutes * 60, config = config)
    )
    val snapshot: StateFlow<UiSnapshot> = _snapshot

    fun send(cmd: SessionCommand) { commands.trySend(cmd) }

    fun start() = send(SessionCommand.Start)
    fun pause() = send(SessionCommand.Pause)
    fun stop() = send(SessionCommand.Stop)
    fun adjust(workMinutes: Int? = null, restMinutes: Int? = null) = send(SessionCommand.Adjust(workMinutes, restMinutes))

    init {
        scope.launch {
            for (cmd in commands) when (cmd) {
                SessionCommand.Start -> launchWorkerIfNeeded()
                SessionCommand.Pause -> mutex.withLock { setState(PomodoroState.Paused) }
                SessionCommand.Stop -> {
                    worker?.cancel()
                    worker?.join()
                    mutex.withLock {
                        setState(PomodoroState.Stopped, seconds = config.workMinutes * 60)
                    }
                }
                is SessionCommand.Adjust -> mutex.withLock {
                    config = PomodoroConfig(
                        workMinutes = cmd.workMinutes ?: config.workMinutes,
                        restMinutes = cmd.restMinutes ?: config.restMinutes
                    )
                    setState(_snapshot.value.state, seconds = currentSecondsForState(_snapshot.value.state))
                }
            }
        }
    }

    private fun launchWorkerIfNeeded() {
        if (worker?.isActive == true) return
        worker = scope.launch {
            mutex.withLock { if (_snapshot.value.state != PomodoroState.Paused) setState(PomodoroState.Working, config.workMinutes * 60) }
            while (isActive) {
                val state = _snapshot.value.state
                if (state == PomodoroState.Paused || state == PomodoroState.Stopped) {
                    delay(200)
                    continue
                }
                delay(1000)
                mutex.withLock {
                    val secs = _snapshot.value.secondsLeft - 1
                    if (secs > 0) setState(state, secs) else when (state) {
                        PomodoroState.Working -> setState(PomodoroState.Resting, config.restMinutes * 60)
                        PomodoroState.Resting -> setState(PomodoroState.Working, config.workMinutes * 60)
                        else -> {}
                    }
                }
            }
        }
    }

    private fun setState(state: PomodoroState, seconds: Int = _snapshot.value.secondsLeft) {
        _snapshot.value = _snapshot.value.copy(state = state, secondsLeft = seconds, config = config)
    }

    private fun currentSecondsForState(state: PomodoroState): Int = when (state) {
        PomodoroState.Working -> config.workMinutes * 60
        PomodoroState.Resting -> config.restMinutes * 60
        PomodoroState.Paused, PomodoroState.Stopped -> _snapshot.value.secondsLeft
    }
}


