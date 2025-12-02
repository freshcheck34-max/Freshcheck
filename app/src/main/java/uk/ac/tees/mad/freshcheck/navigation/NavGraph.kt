package uk.ac.tees.mad.freshcheck.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uk.ac.tees.mad.freshcheck.ui.screens.addedit.AddEditFoodScreen
import uk.ac.tees.mad.freshcheck.ui.screens.addedit.AddEditFoodViewModel
import uk.ac.tees.mad.freshcheck.ui.screens.auth.AuthScreen
import uk.ac.tees.mad.freshcheck.ui.screens.camera.CameraCaptureScreen
import uk.ac.tees.mad.freshcheck.ui.screens.detail.FoodDetailScreen
import uk.ac.tees.mad.freshcheck.ui.screens.home.HomeScreen
import uk.ac.tees.mad.freshcheck.ui.screens.settings.SettingsScreen
import uk.ac.tees.mad.freshcheck.ui.screens.splash.SplashScreen

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = modifier
    ) {

        // ------------------------------------------------------
        // Splash Screen
        // ------------------------------------------------------
        composable(Routes.SPLASH) {
            SplashScreen(
                goToAuth = {
                    navController.navigate(Routes.AUTH) {
                        popUpTo(0)
                    }
                },
                goToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(0)
                    }
                }
            )
        }

        // ------------------------------------------------------
        // Authentication Screen
        // ------------------------------------------------------
        composable(Routes.AUTH) {
            AuthScreen(
                onSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(0)
                    }
                }
            )
        }

        // ------------------------------------------------------
        // Home / Fridge List Screen
        // ------------------------------------------------------
        composable(Routes.HOME) {
            HomeScreen(
                onAddItem = {
                    navController.navigate(Routes.ADD_ITEM)
                },
//                onOpenItem = { id ->
//                    navController.navigate("detail_item/$id")
//                },
                onOpenSettings = {
                    navController.navigate(Routes.SETTINGS)
                },
                onOpenItem =  { id ->
                    navController.navigate("detail_item/$id")
                }
            )
        }

        // ------------------------------------------------------
        // Add Item Screen
        // ------------------------------------------------------
        composable(Routes.ADD_ITEM) { backStackEntry ->
            val viewModel: AddEditFoodViewModel = hiltViewModel()

            val savedStateHandle = backStackEntry.savedStateHandle

            val image by savedStateHandle
                .getStateFlow<String?>("image", null)
                .collectAsState()

            if (image != null) {
                viewModel.onImageSelected(image)
            }

            AddEditFoodScreen(
                itemId = null,
                imagePath = image,
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() },
                onAddPhoto = { navController.navigate(Routes.CAMERA) }
            )
        }


        // ------------------------------------------------------
        // Edit Item Screen
        // ------------------------------------------------------
        composable(
            route = Routes.EDIT_ITEM,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStack ->
            val itemId = backStack.arguments?.getString("itemId") ?: ""
            val savedStateHandle = backStack.savedStateHandle
            val image by savedStateHandle
                .getStateFlow<String?>("image", null)
                .collectAsState()

            val viewModel: AddEditFoodViewModel = hiltViewModel()

            if (image != null) {
                viewModel.onImageSelected(image)
            }

            AddEditFoodScreen(
                itemId = itemId,
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() },
                onAddPhoto = { navController.navigate(Routes.CAMERA) },
                imagePath = image
            )
        }


        // ------------------------------------------------------
        // Food Detail Screen
        // ------------------------------------------------------
        composable(
            route = Routes.DETAIL_ITEM,
        ) { backStack ->
            val id = backStack.arguments?.getString("itemId") ?: ""
            FoodDetailScreen(
//                id = id,
//                onBack = { navController.popBackStack() },
//                onEdit = {
//                    navController.navigate("edit_item/$id")
//                }
            )
        }

        // ------------------------------------------------------
        // Settings Screen
        // ------------------------------------------------------
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Routes.AUTH) {
                        popUpTo(0)
                    }
                }
            )
        }

        // ------------------------------------------------------
        // Camera Screen
        // ------------------------------------------------------
        composable(Routes.CAMERA) {
            CameraCaptureScreen(
                onCaptured = { path ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("image", path)

                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }
    }
}
