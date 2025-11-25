package uk.ac.tees.mad.freshcheck.ui.screens.camera

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import uk.ac.tees.mad.freshcheck.util.CameraFileUtils
import java.io.File

@Composable
fun CameraCaptureScreen(
    onCaptured: (String) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    LaunchedEffect(Unit) {
        hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Camera objects
    val previewView = remember { PreviewView(context) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    // Captured file path
    var capturedImagePath by remember { mutableStateOf<String?>(null) }

    // Start camera when permission granted
    LaunchedEffect(hasPermission) {
        if (!hasPermission) return@LaunchedEffect

        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        val preview = androidx.camera.core.Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val capture = ImageCapture.Builder().build()
        imageCapture = capture

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            capture
        )
    }

    if (!hasPermission) {
        Text(
            "Camera permission required",
//            color = Color.White,
            modifier = Modifier.padding(20.dp)
        )
        return
    }

    // If an image is captured -> show preview
    if (capturedImagePath != null) {
        ReviewCapturedImage(
            path = capturedImagePath!!,
            onRetake = { capturedImagePath = null },
            onSave = { onCaptured(capturedImagePath!!) }
        )
        return
    }

    // Camera preview + shutter button
    Box(modifier = Modifier.fillMaxSize().padding(20.dp)) {

        // Camera preview box
        AndroidView(
            factory = { previewView },
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(420.dp)
                .clip(RoundedCornerShape(20.dp))
//                .background(Color.Black)
                .align(Alignment.TopCenter)
        )
        // CANCEL (bottom-left)
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier
                .align(Alignment.BottomStart)
//                .padding(30.dp)
        ) {
            Text("Cancel")
        }

        // Shutter button
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
                .clickable {
                    val photoFile = CameraFileUtils.createImageFile(context)
                    val output = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                    imageCapture?.takePicture(
                        output,
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                capturedImagePath = photoFile.absolutePath
                            }

                            override fun onError(exception: ImageCaptureException) {
                                Log.e("Camera", "Error: ${exception.message}")
                            }
                        }
                    )
                }
        )
    }
}


@Composable
fun ReviewCapturedImage(
    path: String,
    onRetake: () -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = File(path),
            contentDescription = null,
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = onRetake,
                modifier = Modifier.weight(1f)
            ) {
                Text("Retake")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f)
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
fun CameraCaptureScreenPreviewContent(
    onCancel: () -> Unit = {},
    onShutterClick: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // Fake camera preview box
        Box(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(420.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.DarkGray)
                .align(Alignment.TopCenter),
            contentAlignment = Alignment.Center
        ) {
            Text("Camera Preview", color = Color.White)
        }

        // Cancel button
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(30.dp)
        ) {
            Text("Cancel")
        }

        // Shutter button
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.Black)
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp)
                .clickable { onShutterClick() }
        )
    }
}
@Preview(showSystemUi = true)
@Composable
fun CameraCaptureScreen_Preview() {
    CameraCaptureScreenPreviewContent()
}
