package uk.ac.tees.mad.freshcheck.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import uk.ac.tees.mad.freshcheck.domain.model.FoodItem
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FoodCard(
    item: FoodItem,
    onClick: () -> Unit
) {
    val daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), item.expiryDate)

    val color = when {
        daysLeft <= 2 -> Color.Red
        daysLeft <= 7 -> Color(0xFFFF9800) // orange
        else -> Color(0xFF4CAF50) // green
    }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = item.imageUrl ?: item.localImagePath,
                contentDescription = null,
                modifier = Modifier
                    .size(70.dp)
                    .background(Color.LightGray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium)
                Text(item.category, color = Color.Gray)
            }

            Text(
                "$daysLeft d",
                color = color,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
