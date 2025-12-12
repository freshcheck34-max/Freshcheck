package uk.ac.tees.mad.freshcheck.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.freshcheck.ui.components.SettingClickableRow
import uk.ac.tees.mad.freshcheck.ui.components.SettingSwitch
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(id = android.R.drawable.ic_menu_revert),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // DAILY REMINDER TOGGLE
            SettingSwitch(
                title = "Daily Reminder",
                checked = state.dailyReminderEnabled,
                onCheckedChange = { viewModel.toggleDailyReminder() }
            )

            // REMINDER TIME
            if (state.dailyReminderEnabled) {
                SettingClickableRow(
                    title = "Reminder Time",
                    value = state.dailyReminderTime,
                    onClick = {
                        // Dummy for now — Commit 18 will add TimePicker + DataStore saving
                        viewModel.updateReminderTime("10:00")
                    }
                )
            }

//            Divider()
//
//            // BIOMETRIC LOGIN
//            SettingSwitch(
//                title = "Biometric Login",
//                checked = state.biometricEnabled,
//                onCheckedChange = { viewModel.toggleBiometric() }
//            )

            Divider()

            Button(
                onClick = { viewModel.clearCache() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear Cache")
            }

            // EXPORT DATA
            OutlinedButton(
                onClick = {
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Export Data")
            }

            Spacer(modifier = Modifier.weight(1f))

            // LOGOUT
            Button(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.errorContainer)
            ) {
                Text("Logout")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenUIOnly() {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            painterResource(id = android.R.drawable.ic_menu_revert),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // DAILY REMINDER TOGGLE
            SettingSwitch(
                title = "Daily Reminder",
                checked = true,
                onCheckedChange = { }
            )

            // REMINDER TIME
            SettingClickableRow(
                title = "Reminder Time",
                value = "09:00",
                onClick = { }
            )

            Divider()

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear Cache")
            }

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Export Data")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.errorContainer)
            ) {
                Text("Logout")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenUIPreview() {
    SettingsScreenUIOnly()
}
