package uk.ac.tees.mad.freshcheck

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.freshcheck.navigation.NavGraph
import uk.ac.tees.mad.freshcheck.ui.theme.FreshCheckTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FreshCheckTheme {
                NavGraph()
            }
        }
    }
}

