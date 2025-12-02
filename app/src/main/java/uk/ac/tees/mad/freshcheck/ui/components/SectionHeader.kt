package uk.ac.tees.mad.freshcheck.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp

@Composable
fun SectionHeader(
    title: String,
    color: Color
) {
    Text(
        text = title,
        fontSize = 18.sp,
        color = color
    )
}
