package tasks.coroutines.pomodoro.app

import com.googlecode.lanterna.TerminalPosition
import com.googlecode.lanterna.input.KeyStroke
import com.googlecode.lanterna.input.KeyType
import com.googlecode.lanterna.terminal.DefaultTerminalFactory
import kotlinx.coroutines.*
import tasks.coroutines.pomodoro.PomodoroConfig
import tasks.coroutines.pomodoro.PomodoroState
import tasks.coroutines.pomodoro.session.PomodoroSession

fun main() = runBlocking {
    val terminalFactory = DefaultTerminalFactory()
    terminalFactory.setForceTextTerminal(false)
    terminalFactory.setPreferTerminalEmulator(true)
    val terminal = terminalFactory.createTerminal()
    terminal.enterPrivateMode()

    val scope = this
    var sessions = mutableListOf<PomodoroSession>()
    var selected = 0
    var nextId = 1

    try {
        while (isActive) {
            // Рендер
            terminal.clearScreen()
            terminal.cursorPosition = TerminalPosition(0, 0)
            terminal.putString("Pomodoro (N=new, Space=start/pause, S=stop, +/-=work len, Arrows=select, Esc=exit)")

            sessions.forEachIndexed { index, s ->
                val snap = s.snapshot.value
                val marker = if (index == selected) ">" else " "
                val line = "%s #%d %-12s %8s %4dm%02ds  w:%d r:%d".format(
                    marker, snap.id, snap.name.take(12), snap.state,
                    snap.secondsLeft / 60, snap.secondsLeft % 60,
                    snap.config.workMinutes, snap.config.restMinutes
                )
                terminal.cursorPosition = TerminalPosition(0, index + 2)
                terminal.putString(line)
            }
            terminal.flush()

            // Ввод
            val key: KeyStroke = terminal.readInput()
            when (key.keyType) {
                KeyType.Escape -> break
                KeyType.Character -> when (key.character.lowercaseChar()) {
                    'n' -> {
                        val s = PomodoroSession(nextId++, "task$nextId", PomodoroConfig(), scope)
                        sessions.add(s)
                        selected = sessions.lastIndex
                    }
                    's' -> sessions.getOrNull(selected)?.stop()
                    ' ' -> {
                        val s = sessions.getOrNull(selected)
                        if (s != null) {
                            val st = s.snapshot.value.state
                            if (st == PomodoroState.Paused || st == PomodoroState.Stopped) s.start() else s.pause()
                        }
                    }
                    '+' -> sessions.getOrNull(selected)?.adjust(workMinutes = (sessions[selected].snapshot.value.config.workMinutes + 1))
                    '-' -> sessions.getOrNull(selected)?.let { cur ->
                        val w = (cur.snapshot.value.config.workMinutes - 1).coerceAtLeast(1)
                        cur.adjust(workMinutes = w)
                    }
                    else -> {}
                }
                KeyType.ArrowUp -> if (sessions.isNotEmpty()) selected = (selected - 1 + sessions.size) % sessions.size
                KeyType.ArrowDown -> if (sessions.isNotEmpty()) selected = (selected + 1) % sessions.size
                else -> {}
            }
        }
    } finally {
        terminal.exitPrivateMode()
    }
}


