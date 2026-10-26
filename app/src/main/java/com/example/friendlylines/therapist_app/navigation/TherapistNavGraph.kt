package com.example.friendlylines.therapist_app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.friendlylines.R
import com.example.friendlylines.components.InfoSplashScreen
import com.example.friendlylines.therapist_app.ui.configuration.add.LearningStepsPatternAddScreen
import com.example.friendlylines.therapist_app.ui.configuration.config.LearningStepPatternConfigScreen
import com.example.friendlylines.therapist_app.ui.configuration.list.LearningStepsListScreen
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepsSettingsScreen
import com.example.friendlylines.therapist_app.ui.home.HomeScreen
import com.example.friendlylines.therapist_app.ui.materials.create_new.CreatePatternScreen
import com.example.friendlylines.therapist_app.ui.materials.gallery.GalleryScreen
import kotlinx.coroutines.delay

@Composable
fun TherapistNavGraph(
    navController: NavHostController = rememberNavController(),
    onPlayRequested: () -> Unit = {},
) {
    val onHomeClick: () -> Unit = {
        navController.navigate(TherapistNavRoutes.Home) {
            launchSingleTop = true
        }
    }

    val onBackClick: () -> Unit = {
        if (navController.previousBackStackEntry != null) {
            navController.popBackStack()
        }
    }

    NavHost(navController = navController, startDestination = TherapistNavRoutes.InfoSplash()) {
        composable<TherapistNavRoutes.InfoSplash> { backStackEntry ->
            val route = backStackEntry.toRoute<TherapistNavRoutes.InfoSplash>()

            val navigateToHome: () -> Unit = {
                if (navController.previousBackStackEntry != null) {
                    navController.popBackStack()
                } else {
                    navController.navigate(TherapistNavRoutes.Home) {
                        popUpTo<TherapistNavRoutes.InfoSplash> { inclusive = true }
                    }
                }
            }

            if (route.autoAdvance) {
                LaunchedEffect(Unit) {
                    delay(5000L)
                    navigateToHome()
                }
            }

            InfoSplashScreen(
                appTitle = stringResource(R.string.app_name),
                appIconRes = R.drawable.app_icon,
                onContinue = navigateToHome
            )
        }

        composable<TherapistNavRoutes.Home> {
            HomeScreen(
                onGalleryClick = { navController.navigate(TherapistNavRoutes.Gallery) },
                onLearningStepsClick = { navController.navigate(TherapistNavRoutes.LearningStepsList) },
                onBackClick = { navController.navigate(TherapistNavRoutes.InfoSplash(autoAdvance = false)) },
                onHomeClick = onHomeClick
            )
        }

        composable<TherapistNavRoutes.Gallery> {
            GalleryScreen(
                navController = navController,
                onBackClick = onBackClick,
                onHomeClick = onHomeClick,
                onCreateClick = {navController.navigate(TherapistNavRoutes.PatternCreate)}
            )
        }

        composable<TherapistNavRoutes.PatternCreate> {
            CreatePatternScreen(
                navController = navController,
                onBackClick = onBackClick,
                onHomeClick = onHomeClick,
                onSaveSuccess = { patternId ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("newPatternId", patternId)
                    onBackClick()
                },
            )
        }

        composable<TherapistNavRoutes.LearningStepsList> {
            LearningStepsListScreen(
                navController = navController,
                onBackClick = onBackClick,
                onHomeClick = onHomeClick,
                onCreateClick = { navController.navigate(TherapistNavRoutes.LearningStepsCreate()) },
                onPlayClick = onPlayRequested
            )
        }

        composable<TherapistNavRoutes.LearningStepsCreate> { backStackEntry ->
            val route = backStackEntry.toRoute<TherapistNavRoutes.LearningStepsCreate>()

            LearningStepsSettingsScreen(
                navController = navController,
                stepId = route.stepId,
                onBackClick = onBackClick,
                onHomeClick = onHomeClick,
            )
        }

        composable<TherapistNavRoutes.LearningStepsPatternAdd> { backStackEntry ->
            val route = backStackEntry.toRoute<TherapistNavRoutes.LearningStepsPatternAdd>()

            LearningStepsPatternAddScreen(
                navController = navController,
                stepId = route.stepId,
                configId = route.configId,
                selectedPatternId = route.patternId,
                order = route.order ?: 0,
                onBackClick = onBackClick,
                onHomeClick = onHomeClick,
                onNextClick = { selectedPatternId ->
                    navController.navigate(
                        TherapistNavRoutes.LearningStepsPatternConfig(
                            stepId = route.stepId,
                            patternId = selectedPatternId,
                            configId = route.configId,
                            order = route.order ?: 0
                        )
                    )
                }
            )
        }

        composable<TherapistNavRoutes.LearningStepsPatternConfig> { backStackEntry ->
            val route = backStackEntry.toRoute<TherapistNavRoutes.LearningStepsPatternConfig>()

            LearningStepPatternConfigScreen(
                navController = navController,
                patternId = route.patternId,
                stepId = route.stepId,
                configId = route.configId,
                order = route.order ?: 0,
                onBackClick = onBackClick,
                onHomeClick = onHomeClick,
                onSaveClick = {
                    navController.popBackStack(
                        route = TherapistNavRoutes.LearningStepsCreate(
                            stepId = route.stepId
                        ),
                        inclusive = false
                    )
                }
            )
        }
    }
}