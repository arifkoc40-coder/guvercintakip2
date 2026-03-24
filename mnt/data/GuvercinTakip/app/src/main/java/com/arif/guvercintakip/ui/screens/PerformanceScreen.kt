@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.arif.guvercintakip.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arif.guvercintakip.data.FlightPerformanceEntity
import com.arif.guvercintakip.data.PigeonEntity
import com.arif.guvercintakip.util.toDateText

@Composable
fun PerformanceScreen(
    pigeons: List<PigeonEntity>,
    performances: List<FlightPerformanceEntity>,
    onSave: (Long, String, String, String, Boolean) -> Unit
) {
    var selectedId by rememberSaveable { mutableStateOf(pigeons.firstOrNull()?.id ?: 0L) }
    var duration by rememberSaveable { mutableStateOf("") }
    var flips by rememberSaveable { mutableStateOf("") }
    var note by rememberSaveable { mutableStateOf("") }
    var best by rememberSaveable { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    val selectedPigeon = pigeons.firstOrNull { it.id == selectedId }

    LazyColumn(contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                        OutlinedTextField(
                            value = selectedPigeon?.name?.ifBlank { selectedPigeon.ringNumber } ?: "Güvercin seç",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Güvercin") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth()
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            pigeons.forEach { pigeon ->
                                DropdownMenuItem(
                                    text = { Text(pigeon.name.ifBlank { pigeon.ringNumber }) },
                                    onClick = {
                                        selectedId = pigeon.id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                    OutlinedTextField(value = duration, onValueChange = { duration = it.filter(Char::isDigit) }, label = { Text("Uçuş süresi (dk)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = flips, onValueChange = { flips = it.filter(Char::isDigit) }, label = { Text("Takla sayısı (isteğe bağlı)") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = note, onValueChange = { note = it }, label = { Text("Günlük not") }, modifier = Modifier.fillMaxWidth())
                    Column {
                        Checkbox(checked = best, onCheckedChange = { best = it })
                        Text("En iyi performans olarak işaretle")
                    }
                    Button(onClick = {
                        if (selectedId != 0L) {
                            onSave(selectedId, duration, flips, note, best)
                            duration = ""
                            flips = ""
                            note = ""
                            best = false
                        }
                    }, modifier = Modifier.fillMaxWidth()) {
                        Text("Performans Kaydet")
                    }
                }
            }
        }
        items(performances) { perf ->
            val pigeon = pigeons.firstOrNull { it.id == perf.pigeonId }
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(pigeon?.name?.ifBlank { pigeon.ringNumber } ?: "Silinmiş kayıt", style = MaterialTheme.typography.titleMedium)
                    Text("Tarih: ${perf.flightDate.toDateText()}")
                    Text("Süre: ${perf.durationMinutes} dk")
                    Text("Takla: ${perf.flipCount ?: "-"}")
                    if (perf.bestPerformance) Text("⭐ En iyi performans")
                    if (perf.dailyNote.isNotBlank()) Text("Not: ${perf.dailyNote}")
                }
            }
        }
    }
}
