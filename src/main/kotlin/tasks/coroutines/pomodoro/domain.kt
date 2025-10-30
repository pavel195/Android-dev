package tasks.coroutines.pomodoro

enum class PomodoroState { Working, Resting, Paused, Stopped }

data class PomodoroConfig(
    val workMinutes: Int = 25,
    val restMinutes: Int = 5
) {
    init {
        require(workMinutes > 0) { "workMinutes > 0" }
        require(restMinutes > 0) { "restMinutes > 0" }
    }
}

data class UiSnapshot(
    val id: Int,
    val name: String,
    val state: PomodoroState,
    val secondsLeft: Int,
    val config: PomodoroConfig
)


