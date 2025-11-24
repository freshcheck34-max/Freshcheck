package uk.ac.tees.mad.freshcheck.ui.screens.addedit

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.util.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
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
@Preview(showSystemUi = true)
@Composable
fun PreviewAddEdit() {
    FreshCheckTheme {
        AddEditFoodScreen(
            onBack = {},
            onSave = {},
            onAddPhoto = {}
        )
    }
}
