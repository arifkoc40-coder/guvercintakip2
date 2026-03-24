package com.arif.guvercintakip.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import coil.compose.AsyncImage
import com.arif.guvercintakip.data.PigeonEntity

@Composable
fun AddPigeonScreen(
    editingPigeon: PigeonEntity?,
    onSave: (Long, Uri?, String?, String, String, String, String, String, String) -> Unit
) {
    var name by rememberSaveable(editingPigeon?.id) { mutableStateOf(editingPigeon?.name ?: "") }
    var ring by rememberSaveable(editingPigeon?.id) { mutableStateOf(editingPigeon?.ringNumber ?: "") }
    var color by rememberSaveable(editingPigeon?.id) { mutableStateOf(editingPigeon?.color ?: "") }
    var breed by rememberSaveable(editingPigeon?.id) { mutableStateOf(editingPigeon?.breed ?: "Taklacı") }
    var birthYear by rememberSaveable(editingPigeon?.id) { mutableStateOf(editingPigeon?.birthYear?.toString() ?: "") }
    var notes by rememberSaveable(editingPigeon?.id) { mutableStateOf(editingPigeon?.notes ?: "") }
    var selectedImage by remember(editingPigeon?.id) { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        selectedImage = uri
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    selectedImage?.let {
                        AsyncImage(model = it, contentDescription = null, modifier = Modifier.fillMaxWidth().height(200.dp))
                    } ?: editingPigeon?.photoPath?.let {
                        AsyncImage(model = it, contentDescription = null, modifier = Modifier.fillMaxWidth().height(200.dp))
                    }
                    Button(onClick = { launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }) {
                        Text("Fotoğraf seç")
                    }
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("İsim") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = ring, onValueChange = { ring = it }, label = { Text("Halka numarası *") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = color, onValueChange = { color = it }, label = { Text("Renk") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = breed, onValueChange = { breed = it }, label = { Text("Cins") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = birthYear, onValueChange = { birthYear = it.filter(Char::isDigit) }, label = { Text("Doğum yılı") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notlar") }, modifier = Modifier.fillMaxWidth())
                    Button(
                        onClick = {
                            onSave(editingPigeon?.id ?: 0, selectedImage, editingPigeon?.photoPath, name, ring, color, breed, birthYear, notes)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (editingPigeon == null) "Güvercin Kaydet" else "Güncelle")
                    }
                }
            }
        }
    }
}
