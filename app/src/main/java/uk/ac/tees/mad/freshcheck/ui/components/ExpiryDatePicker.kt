package uk.ac.tees.mad.freshcheck.ui.components

import android.app.DatePickerDialog
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.time.LocalDate
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpiryDatePicker(
    date: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val context = LocalContext.current

    OutlinedButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            val now = Calendar.getInstance()
            val dialog = DatePickerDialog(
                context,
                { _, y, m, d ->
                    onDateSelected(LocalDate.of(y, m + 1, d))
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }
    ) {
        Text(date?.toString() ?: "Select Expiry Date")
    }
}
