package com.arif.guvercintakip.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arif.guvercintakip.data.FlightPerformanceEntity
import com.arif.guvercintakip.data.PigeonEntity
import com.arif.guvercintakip.util.toDateText

@Composable
fun PigeonsScreen(
    pigeons: List<PigeonEntity>,
    performances: List<FlightPerformanceEntity>,
    onEdit: (PigeonEntity) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(pigeons, key = { it.id }) { pigeon ->
            val last = performances.filter { it.pigeonId == pigeon.id }.maxByOrNull { it.flightDate }
            Card(modifier = Modifier.fillMaxWidth().clickable { onEdit(pigeon) }) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (pigeon.photoPath != null) {
                        AsyncImage(
                            model = pigeon.photoPath,
                            contentDescription = pigeon.name,
                            modifier = Modifier.fillMaxWidth().height(180.dp)
                        )
                    }
                    Text(pigeon.name.ifBlank { "İsimsiz güvercin" }, style = MaterialTheme.typography.titleMedium)
                    Text("Halka no: ${pigeon.ringNumber}")
                    Text("Renk: ${pigeon.color.ifBlank { "-" }} | Cins: ${pigeon.breed}")
                    Text("Doğum yılı: ${pigeon.birthYear?.toString() ?: "-"}")
                    Text("Son uçuş: ${last?.flightDate?.toDateText() ?: "Kayıt yok"}")
                    if (pigeon.notes.isNotBlank()) Text("Not: ${pigeon.notes}")
                }
            }
        }
    }
}
