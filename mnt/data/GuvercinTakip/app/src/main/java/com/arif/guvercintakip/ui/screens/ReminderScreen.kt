package com.arif.guvercintakip.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arif.guvercintakip.util.ReminderSettings

@Composable
fun ReminderScreen(settings: ReminderSettings, onSave: (ReminderSettings) -> Unit) {
    var vitamin by rememberSaveable { mutableStateOf(settings.vitaminEnabled) }
    var feed by rememberSaveable { mutableStateOf(settings.feedEnabled) }
    var pairing by rememberSaveable { mutableStateOf(settings.pairingEnabled) }
    var cleaning by rememberSaveable { mutableStateOf(settings.cleaningEnabled) }
    var hour by rememberSaveable { mutableStateOf(settings.hour.toString()) }
    var minute by rememberSaveable { mutableStateOf(settings.minute.toString()) }

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ReminderToggle("Vitamin günü", vitamin) { vitamin = it }
                    ReminderToggle("Yem değişimi", feed) { feed = it }
                    ReminderToggle("Eşleme zamanı", pairing) { pairing = it }
                    ReminderToggle("Temizlik günü", cleaning) { cleaning = it }
                    OutlinedTextField(value = hour, onValueChange = { hour = it.filter(Char::isDigit).take(2) }, label = { Text("Saat") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = minute, onValueChange = { minute = it.filter(Char::isDigit).take(2) }, label = { Text("Dakika") }, modifier = Modifier.fillMaxWidth())
                    Button(onClick = {
                        onSave(
                            ReminderSettings(
                                vitaminEnabled = vitamin,
                                feedEnabled = feed,
                                pairingEnabled = pairing,
                                cleaningEnabled = cleaning,
                                hour = (hour.toIntOrNull() ?: 9).coerceIn(0, 23),
                                minute = (minute.toIntOrNull() ?: 0).coerceIn(0, 59)
                            )
                        )
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Hatırlatıcıları Kaydet")
                    }
                }
            }
        }
    }
}

@Composable
private fun ReminderToggle(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
