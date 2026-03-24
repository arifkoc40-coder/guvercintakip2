package com.arif.guvercintakip

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.arif.guvercintakip.data.PigeonEntity
import com.arif.guvercintakip.ui.MainViewModel
import com.arif.guvercintakip.ui.screens.AddPigeonScreen
import com.arif.guvercintakip.ui.screens.HomeScreen
import com.arif.guvercintakip.ui.screens.PerformanceScreen
import com.arif.guvercintakip.ui.screens.PigeonsScreen
import com.arif.guvercintakip.ui.screens.ReminderScreen
import com.arif.guvercintakip.ui.screens.SettingsScreen

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                AppContent(viewModel)
            }
        }
    }
}

enum class Destinations(val route: String, val label: String) {
    Home("home", "Ana Sayfa"),
    Pigeons("pigeons", "Güvercinlerim"),
    Add("add", "Güvercin Ekle"),
    Performance("performance", "Performans"),
    Reminder("reminder", "Hatırlatıcı"),
    Settings("settings", "Ayarlar")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppContent(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    val pigeons by viewModel.pigeons.collectAsStateWithLifecycle()
    val performances by viewModel.performances.collectAsStateWithLifecycle()
    val stats by viewModel.stats.collectAsStateWithLifecycle()
    val reminderSettings by viewModel.reminders.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()
    val snackHostState = remember { SnackbarHostState() }
    var editingPigeon by remember { mutableStateOf<PigeonEntity?>(null) }

    if (Build.VERSION.SDK_INT >= 33) {
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {}
        LaunchedEffect(Unit) { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) }
    }

    LaunchedEffect(message) {
        message?.let {
            snackHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackHostState) },
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Destinations.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Destinations.Home.route) {
                HomeScreen(stats = stats, pigeons = pigeons, performances = performances)
            }
            composable(Destinations.Pigeons.route) {
                PigeonsScreen(
                    pigeons = pigeons,
                    performances = performances,
                    onEdit = {
                        editingPigeon = it
                        navController.navigate(Destinations.Add.route)
                    }
                )
            }
            composable(Destinations.Add.route) {
                AddPigeonScreen(
                    editingPigeon = editingPigeon,
                    onSave = { id, uri, currentPath, name, ring, color, breed, birthYear, notes ->
                        viewModel.savePigeon(id, uri, currentPath, name, ring, color, breed, birthYear, notes)
                        editingPigeon = null
                        navController.navigate(Destinations.Pigeons.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Destinations.Performance.route) {
                PerformanceScreen(
                    pigeons = pigeons,
                    performances = performances,
                    onSave = { pigeonId, duration, flip, note, best ->
                        viewModel.addPerformance(pigeonId, duration, flip, note, best)
                    }
                )
            }
            composable(Destinations.Reminder.route) {
                ReminderScreen(
                    settings = reminderSettings,
                    onSave = viewModel::saveReminderSettings
                )
            }
            composable(Destinations.Settings.route) {
                SettingsScreen(
                    onExport = viewModel::exportBackup,
                    onImport = viewModel::importBackup
                )
            }
        }
    }
}

@Composable
private fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        Destinations.Home to Icons.Default.Home,
        Destinations.Pigeons to Icons.AutoMirrored.Filled.List,
        Destinations.Add to Icons.Default.Add,
        Destinations.Performance to Icons.Default.ShowChart,
        Destinations.Reminder to Icons.Default.Notifications,
        Destinations.Settings to Icons.Default.Settings
    )
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    NavigationBar {
        items.forEach { (destination, icon) ->
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = {
                    navController.navigate(destination.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icon, contentDescription = destination.label) },
                label = { Text(destination.label) }
            )
        }
    }
}
