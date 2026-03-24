package com.arif.guvercintakip.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arif.guvercintakip.data.FlightPerformanceEntity
import com.arif.guvercintakip.data.PigeonEntity
import com.arif.guvercintakip.data.StatsSummary
import com.arif.guvercintakip.util.toDateText

@Composable
fun HomeScreen(
    stats: StatsSummary,
    pigeons: List<PigeonEntity>,
    performances: List<FlightPerformanceEntity>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { StatCard("Toplam güvercin sayısı", stats.totalPigeons.toString()) }
        item { StatCard("En çok uçan güvercin", stats.mostFlyingName) }
        item { StatCard("En iyi takla atan", stats.bestFlipName) }
        item { StatCard("Son uçuş tarihi", stats.lastFlightDate?.toDateText() ?: "-") }
        item {
            Text("Son kayıtlar", style = MaterialTheme.typography.titleMedium)
        }
        items(performances.take(5)) { perf ->
            val pigeonName = pigeons.firstOrNull { it.id == perf.pigeonId }?.name?.ifBlank { it.ringNumber } ?: "Bilinmeyen"
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(pigeonName, style = MaterialTheme.typography.titleMedium)
                    Text("Uçuş süresi: ${perf.durationMinutes} dk")
                    Text("Takla sayısı: ${perf.flipCount ?: "-"}")
                    Text("Tarih: ${perf.flightDate.toDateText()}")
                }
            }
        }
    }
}

@Composable
private fun StatCard(title: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
