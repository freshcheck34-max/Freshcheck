package uk.ac.tees.mad.freshcheck.ui.screens.addedit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import uk.ac.tees.mad.freshcheck.ui.components.CategoryDropdown
import uk.ac.tees.mad.freshcheck.ui.components.ExpiryDatePicker
import uk.ac.tees.mad.freshcheck.ui.components.PhotoPickerSection
import uk.ac.tees.mad.freshcheck.ui.theme.FreshCheckTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditFoodScreen(
    viewModel: AddEditFoodViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSave: () -> Unit,
    onAddPhoto: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (state.isEditing) "Edit Item" else "Add Item") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(20.dp)
        ) {

            OutlinedTextField(
                value = state.name,
                onValueChange = viewModel::onNameChange,
                label = { Text("Food Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            CategoryDropdown(
                selected = state.category,
                onSelect = viewModel::onCategoryChange
            )

            Spacer(Modifier.height(16.dp))

            ExpiryDatePicker(
                date = state.expiryDate,
                onDateSelected = viewModel::onExpiryChange
            )

            Spacer(Modifier.height(16.dp))

            PhotoPickerSection(
                imagePath = state.imagePath,
                onAddPhoto = onAddPhoto
            )

            Spacer(Modifier.height(32.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSave
            ) {
                Text(if (state.isEditing) "Save Changes" else "Add Item")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditFoodScreenPreview(
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    onAddPhoto: () -> Unit = {}
) {
    // Preview-safe mock state
    var name by remember { mutableStateOf("Milk") }
    var category by remember { mutableStateOf("Dairy") }
    var expiryDate by remember { mutableStateOf(java.time.LocalDate.now()) }
    var imagePath by remember { mutableStateOf<String?>(null) }
    val isEditing = false

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Item" else "Add Item") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            Modifier
                .padding(padding)
                .padding(20.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Food Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            CategoryDropdown(
                selected = category,
                onSelect = { category = it }
            )

            Spacer(Modifier.height(16.dp))

            ExpiryDatePicker(
                date = expiryDate,
                onDateSelected = { expiryDate = it }
            )

            Spacer(Modifier.height(16.dp))

            PhotoPickerSection(
                imagePath = imagePath,
                onAddPhoto = onAddPhoto
            )

            Spacer(Modifier.height(32.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onSave
            ) {
                Text(if (isEditing) "Save Changes" else "Add Item")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun AddEditFoodScreenPreview_UI() {
    FreshCheckTheme {
        AddEditFoodScreenPreview()
    }
}

