package uk.ac.tees.mad.freshcheck.ui.screens.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.tees.mad.freshcheck.ui.theme.FreshCheckTheme

@Composable
fun SplashScreen(
    goToAuth: () -> Unit,
    goToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    var animateLogo by remember { mutableStateOf(false) }

    val scale = animateFloatAsState(
        targetValue = if (animateLogo) 1f else 0.7f,
        animationSpec = tween(1200)
    ).value

    val alpha = animateFloatAsState(
        targetValue = if (animateLogo) 1f else 0f,
        animationSpec = tween(1200)
    ).value

    LaunchedEffect(true) {
        animateLogo = true
    }

    // Navigate when session status is ready
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn != null) {
            if (isLoggedIn == true) goToHome() else goToAuth()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(160.dp)
                .scale(scale)
                .alpha(alpha)
        ) {
            // Minimal logo placeholder (will be updated later)
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(alpha),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(color = Color(0xFF7ED957)) // pastel green
                }
            }
        }
    }
}
