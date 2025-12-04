package uk.ac.tees.mad.freshcheck.ui.screens.detail

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import uk.ac.tees.mad.freshcheck.domain.model.FoodItem
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(
    id: String,
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
    onDeleteRequested: (FoodItem) -> Unit,
    onMarkConsumedRequested: (FoodItem) -> Unit,
    viewModel: FoodDetailViewModel = hiltViewModel()
) {
    val ctx = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    // Load item on first composition
    LaunchedEffect(id) {
        viewModel.loadItem(id)
    }

    val item = state.item

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item?.name ?: "Item") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(painterResource(id = android.R.drawable.ic_menu_close_clear_cancel), null)
                    }
                },
                actions = {
                    IconButton(onClick = { item?.let { onEdit(it.id) } }) {
                        Icon(painterResource(id = android.R.drawable.ic_menu_edit), null)
                    }
                }
            )
        },
        bottomBar = {
            if (item != null) {
                BottomAppBar(
                    contentPadding = PaddingValues(12.dp)
                ) {
                    Button(
                        onClick = { onMarkConsumedRequested(item) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Mark as Consumed")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    OutlinedButton(
                        onClick = { onDeleteRequested(item) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Delete")
                    }
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (state.loading) {
                CircularProgressIndicator()
                return@Column
            }

            if (state.error != null) {
                Text(text = state.error ?: "Unknown error", color = MaterialTheme.colorScheme.error)
                return@Column
            }

            item?.let { food ->
                // Image
                if (!food.imageUrl.isNullOrBlank() || !food.localImagePath.isNullOrBlank()) {
                    AsyncImage(
                        model = food.imageUrl ?: food.localImagePath,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .padding(bottom = 12.dp),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                } else {
                    // placeholder
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp),
                        shape = RoundedCornerShape(12.dp),
                        tonalElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("No photo", fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Name & category
                Text(food.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(food.category, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

                Spacer(Modifier.height(12.dp))

                // Dates and days remaining
                val daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), food.expiryDate)
                val color = when {
                    daysLeft <= 0 -> MaterialTheme.colorScheme.error
                    daysLeft <= 2 -> MaterialTheme.colorScheme.error
                    daysLeft <= 7 -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.primary
                }

                Text("Added: ${food.addedDate}", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(6.dp))
                Text("Expiry: ${food.expiryDate}", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(8.dp))

                Text(
                    text = when {
                        daysLeft < 0 -> "Expired ${-daysLeft} day(s) ago"
                        daysLeft == 0L -> "Expires today"
                        else -> "$daysLeft day(s) remaining"
                    },
                    color = color,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(Modifier.height(16.dp))

                // Share row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(onClick = {
                        val shareText = "${food.name} expires ${when {
                            daysLeft < 0 -> "${-daysLeft} day(s) ago"
                            daysLeft == 0L -> "today"
                            else -> "in $daysLeft day(s)"
                        }}"
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        ctx.startActivity(Intent.createChooser(intent, "Share item"))
                    }) {
                        Text("Share")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(onClick = { /* future: archive or more options */ }) {
                        Text("More")
                    }
                }
            }
        }
    }
}
