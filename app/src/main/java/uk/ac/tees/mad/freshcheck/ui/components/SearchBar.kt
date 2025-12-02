package uk.ac.tees.mad.freshcheck.ui.components

import android.R
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text("Search food...") },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.ic_menu_search),
                contentDescription = null
            )
        }
    )
}
