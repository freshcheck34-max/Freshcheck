package uk.ac.tees.mad.freshcheck.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.freshcheck.domain.model.FoodItem
import uk.ac.tees.mad.freshcheck.ui.components.FoodCard
import uk.ac.tees.mad.freshcheck.ui.components.SearchBar
import uk.ac.tees.mad.freshcheck.ui.components.SectionHeader
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddItem: () -> Unit,
    onOpenItem: (String) -> Unit,
    onOpenSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.loadItems()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddItem) {
                Icon(
                    painterResource(id = android.R.drawable.ic_input_add),
                    contentDescription = null
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("FreshCheck") },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            painterResource(id = android.R.drawable.ic_menu_preferences),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->

        val items = state.items.filter {
            it.name.contains(state.searchQuery, ignoreCase = true)
        }

        val today = LocalDate.now()

        val expiringSoon = items.filter {
            ChronoUnit.DAYS.between(today, it.expiryDate) <= 2
        }
        val thisWeek = items.filter {
            val days = ChronoUnit.DAYS.between(today, it.expiryDate)
            days in 3..7
        }
        val safe = items.filter {
            ChronoUnit.DAYS.between(today, it.expiryDate) > 7
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            // SEARCH BAR
            SearchBar(
                value = state.searchQuery,
                onValueChange = viewModel::onSearchChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            // FULL LIST
            LazyColumn {

                // EXPIRING SOON
                if (expiringSoon.isNotEmpty()) {
                    item {
                        SectionHeader("Expiring Soon", Color.Red)
                        Spacer(Modifier.height(8.dp))
                    }
                    items(expiringSoon) { item ->
                        FoodCard(item = item) {
                            onOpenItem(item.id)
                        }
                    }
                }

                // THIS WEEK
                if (thisWeek.isNotEmpty()) {
                    item { Spacer(Modifier.height(16.dp)) }
                    item {
                        SectionHeader("This Week", Color(0xFFFF9800))
                        Spacer(Modifier.height(8.dp))
                    }
                    items(thisWeek) { item ->
                        FoodCard(item = item) {
                            onOpenItem(item.id)
                        }
                    }
                }

                // SAFE
                if (safe.isNotEmpty()) {
                    item { Spacer(Modifier.height(16.dp)) }
                    item {
                        SectionHeader("Safe", Color(0xFF4CAF50))
                        Spacer(Modifier.height(8.dp))
                    }
                    items(safe) { item ->
                        FoodCard(item = item) {
                            onOpenItem(item.id)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreenPreviewContent(
    items: List<FoodItem> = samplePreviewItems()
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(
                    painterResource(id = android.R.drawable.ic_input_add),
                    contentDescription = null
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("FreshCheck") },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            painterResource(id = android.R.drawable.ic_menu_preferences),
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { padding ->

        val today = LocalDate.now()

        val expiringSoon = items.filter {
            ChronoUnit.DAYS.between(today, it.expiryDate) <= 2
        }
        val thisWeek = items.filter {
            val days = ChronoUnit.DAYS.between(today, it.expiryDate)
            days in 3..7
        }
        val safe = items.filter {
            ChronoUnit.DAYS.between(today, it.expiryDate) > 7
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            SearchBar(
                value = "",
                onValueChange = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {

                if (expiringSoon.isNotEmpty()) {
                    item {
                        SectionHeader("Expiring Soon", Color.Red)
                        Spacer(Modifier.height(8.dp))
                    }
                    items(expiringSoon) { FoodCard(it) {} }
                }

                if (thisWeek.isNotEmpty()) {
                    item { Spacer(Modifier.height(16.dp)) }
                    item {
                        SectionHeader("This Week", Color(0xFFFF9800))
                        Spacer(Modifier.height(8.dp))
                    }
                    items(thisWeek) { FoodCard(it) {} }
                }

                if (safe.isNotEmpty()) {
                    item { Spacer(Modifier.height(16.dp)) }
                    item {
                        SectionHeader("Safe", Color(0xFF4CAF50))
                        Spacer(Modifier.height(8.dp))
                    }
                    items(safe) { FoodCard(it) {} }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun samplePreviewItems(): List<FoodItem> {
    return listOf(
        FoodItem(
            id = "1",
            name = "Milk",
            category = "Dairy",
            addedDate = LocalDate.now(),
            expiryDate = LocalDate.now().plusDays(1),
            imageUrl = null,
            localImagePath = null,
            consumed = false,
            userId = "PREVIEW"
        ),
        FoodItem(
            id = "2",
            name = "Eggs",
            category = "Dairy",
            addedDate = LocalDate.now(),
            expiryDate = LocalDate.now().plusDays(5),
            imageUrl = null,
            localImagePath = null,
            consumed = false,
            userId = "PREVIEW"
        ),
        FoodItem(
            id = "3",
            name = "Apples",
            category = "Fruit",
            addedDate = LocalDate.now(),
            expiryDate = LocalDate.now().plusDays(12),
            imageUrl = null,
            localImagePath = null,
            consumed = false,
            userId = "PREVIEW"
        )
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreen_Preview() {
    HomeScreenPreviewContent()
}

