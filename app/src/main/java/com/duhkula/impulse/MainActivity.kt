package com.duhkula.impulse

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

private const val PREFS = "impulse"
private const val END_TIME = "cooldown_end"
private const val VENT = "vent"
private const val DEFAULT_MINUTES = 30L

enum class Screen { HOME, PAUSE, VENT, RESET, COMPLETE, SETTINGS }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ImpulseApp(applicationContext) }
    }
}

@Composable
fun ImpulseApp(context: Context) {
    val prefs = remember { context.getSharedPreferences(PREFS, Context.MODE_PRIVATE) }
    var screen by remember { mutableStateOf(Screen.HOME) }
    var endTime by remember { mutableLongStateOf(prefs.getLong(END_TIME, 0L)) }
    var vent by remember { mutableStateOf(prefs.getString(VENT, "") ?: "") }
    var cooldownMinutes by remember { mutableLongStateOf(prefs.getLong("minutes", DEFAULT_MINUTES)) }

    fun startPause() {
        val end = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(cooldownMinutes)
        prefs.edit().putLong(END_TIME, end).apply()
        endTime = end
        screen = Screen.PAUSE
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            when (screen) {
                Screen.HOME -> HomeScreen(onPause = ::startPause, onSettings = { screen = Screen.SETTINGS })
                Screen.PAUSE -> PauseScreen(
                    endTime = endTime,
                    onVent = { screen = Screen.VENT },
                    onReset = { screen = Screen.RESET },
                    onFinish = { screen = Screen.COMPLETE }
                )
                Screen.VENT -> VentScreen(
                    initial = vent,
                    onSave = { text ->
                        vent = text
                        prefs.edit().putString(VENT, text).apply()
                        screen = Screen.PAUSE
                    },
                    onDelete = {
                        vent = ""
                        prefs.edit().remove(VENT).apply()
                        screen = Screen.PAUSE
                    }
                )
                Screen.RESET -> ResetScreen(onBack = { screen = Screen.PAUSE })
                Screen.COMPLETE -> CompleteScreen(
                    onAgain = ::startPause,
                    onDone = {
                        prefs.edit().remove(END_TIME).apply()
                        endTime = 0L
                        screen = Screen.HOME
                    }
                )
                Screen.SETTINGS -> SettingsScreen(
                    current = cooldownMinutes,
                    onSet = { minutes ->
                        cooldownMinutes = minutes
                        prefs.edit().putLong("minutes", minutes).apply()
                    },
                    onBack = { screen = Screen.HOME }
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(onPause: () -> Unit, onSettings: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("⚡ IMPULSE", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Pause the impulse. Keep the choice.", textAlign = TextAlign.Center)
        Spacer(Modifier.height(52.dp))
        Button(onClick = onPause, modifier = Modifier.size(210.dp), shape = CircleShape) {
            Text("PAUSE ME", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(32.dp))
        Text("You don't have to act on this right now.", textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        TextButton(onClick = onSettings) { Text("Settings") }
    }
}

@Composable
private fun PauseScreen(endTime: Long, onVent: () -> Unit, onReset: () -> Unit, onFinish: () -> Unit) {
    var remaining by remember { mutableLongStateOf((endTime - System.currentTimeMillis()).coerceAtLeast(0L)) }
    LaunchedEffect(endTime) {
        while (true) {
            remaining = (endTime - System.currentTimeMillis()).coerceAtLeast(0L)
            if (remaining == 0L) break
            delay(1000)
        }
    }
    val minutes = TimeUnit.MILLISECONDS.toMinutes(remaining)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(remaining) % 60
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("PAUSE", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Text(String.format("%02d:%02d", minutes, seconds), fontSize = 64.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text(if (remaining > 0) "You don't have to solve this right now." else "The pause is complete.", textAlign = TextAlign.Center)
        Spacer(Modifier.height(36.dp))
        Button(onClick = onVent, modifier = Modifier.fillMaxWidth()) { Text("GET IT OUT") }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onReset, modifier = Modifier.fillMaxWidth()) { Text("RESET / DISTRACT") }
        Spacer(Modifier.height(10.dp))
        OutlinedButton(onClick = onFinish, modifier = Modifier.fillMaxWidth(), enabled = remaining == 0L) { Text("HOW DO I FEEL?") }
    }
}

@Composable
private fun VentScreen(initial: String, onSave: (String) -> Unit, onDelete: () -> Unit) {
    var text by remember { mutableStateOf(initial) }
    Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
        Text("GET IT OUT", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Write exactly what you want to say. You don't need to make it reasonable or polite. Nobody else has to see it.")
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.fillMaxWidth().height(300.dp),
            placeholder = { Text("Dump everything here…") }
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { onSave(text) }, modifier = Modifier.fillMaxWidth()) { Text("SAVE PRIVATELY") }
        TextButton(onClick = onDelete, modifier = Modifier.fillMaxWidth()) { Text("Delete this vent") }
    }
}

@Composable
private fun ResetScreen(onBack: () -> Unit) {
    val activities = listOf("Take a walk", "Put on music", "Have a shower", "Get some cold water", "Clean one small thing", "Breathe slowly", "Call someone you trust", "Play a game")
    Column(modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState())) {
        Text("RESET", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("You don't need to feel better. Just give the urge something else to do for a few minutes.")
        Spacer(Modifier.height(20.dp))
        activities.forEach { activity ->
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), shape = RoundedCornerShape(14.dp)) {
                Text(activity, modifier = Modifier.padding(20.dp), fontSize = 18.sp)
            }
        }
        Spacer(Modifier.height(12.dp))
        TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back to pause") }
    }
}

@Composable
private fun CompleteScreen(onAgain: () -> Unit, onDone: () -> Unit) {
    var choice by remember { mutableStateOf<String?>(null) }
    val options = listOf("Still activated", "About the same", "A bit calmer", "Much calmer")
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("HOW DO YOU FEEL NOW?", fontSize = 25.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        options.forEach { option ->
            OutlinedButton(onClick = { choice = option }, modifier = Modifier.fillMaxWidth()) { Text(option) }
            Spacer(Modifier.height(8.dp))
        }
        if (choice != null) {
            Spacer(Modifier.height(16.dp))
            Text("You made space between the urge and the action.", textAlign = TextAlign.Center)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onDone, modifier = Modifier.fillMaxWidth()) { Text("I'M DONE") }
            TextButton(onClick = onAgain) { Text("PAUSE AGAIN") }
        }
    }
}

@Composable
private fun SettingsScreen(current: Long, onSet: (Long) -> Unit, onBack: () -> Unit) {
    val choices = listOf(10L, 20L, 30L, 60L, 120L)
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("SETTINGS", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))
        Text("Default cooldown")
        Spacer(Modifier.height(12.dp))
        choices.forEach { minutes ->
            OutlinedButton(onClick = { onSet(minutes) }, modifier = Modifier.fillMaxWidth()) {
                Text("$minutes minutes" + if (minutes == current) "  ✓" else "")
            }
            Spacer(Modifier.height(8.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text("Your vents stay on this device. IMPULSE has no account, cloud sync or advertising in the MVP.", style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(20.dp))
        TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}
