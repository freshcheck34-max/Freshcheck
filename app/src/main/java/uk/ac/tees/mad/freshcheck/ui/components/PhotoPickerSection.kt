package uk.ac.tees.mad.freshcheck.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun PhotoPickerSection(
    imagePath: String?, onAddPhoto: () -> Unit
) {
    Column {
        if (imagePath != null) {
            AsyncImage(
                model = imagePath,
                contentDescription = "Preview",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No Image Selected")
            }
        }

        Spacer(Modifier.height(10.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(), onClick = onAddPhoto
        ) {
            Text("Add Photo")
        }
    }
}
