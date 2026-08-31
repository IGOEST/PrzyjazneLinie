package com.example.friendlylines.therapist_app.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateTopAppBar
import com.example.friendlylines.therapist_app.ui.configuration.add.LearningStepsPatternAddScreen
import com.example.friendlylines.therapist_app.ui.configuration.config.LearningStepPatternConfigScreen
import com.example.friendlylines.therapist_app.ui.configuration.list.LearningStepsListScreen
import com.example.friendlylines.therapist_app.ui.configuration.patterns.LearningStepsPatternsScreen
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepsSettingsScreen
import com.example.friendlylines.therapist_app.ui.materials.create_new.CreatePatternScreen
import com.example.friendlylines.therapist_app.ui.materials.gallery.GalleryScreen
import com.example.friendlylines.therapist_app.ui.theme.Primary1000
import com.example.friendlylines.therapist_app.ui.theme.Primary300
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import com.example.friendlylines.therapist_app.ui.theme.Primary900

object NavRoutes {
    const val MAIN = "main"

    const val GALLERY = "materials"

    const val PATTERN_CREATE = "materials/create"

    const val LEARNING_STEPS_LIST = "learning_steps/list"

    const val LEARNING_STEPS_CREATE = "learning_steps/create"

    const val LEARNING_STEPS_PATTERNS = "learning_steps/patterns"

//    const val LEARNING_STEPS_PATTERN_ADD = "learning_steps/patterns/add"

    const val LEARNING_STEPS_PATTERN_ADD = "learning_steps/patterns/add?configId={configId}&patternId={patternId}&order={order}"

//    const val LEARNING_STEPS_PATTERN_CONFIG = "learning_steps/pattern/config/{patternId}"

    const val LEARNING_STEPS_PATTERN_CONFIG = "learning_steps/pattern/config/{patternId}?configId={configId}&order={order}"

//    fun learningStepsPatternConfig(patternId: Long) = "learning_steps/pattern/config/$patternId"

    fun learningStepsPatternAdd(configId: Long? = null, patternId: Long? = null, order: Int? = null): String {
        val params = buildList {
            configId?.let {
                add("configId=$it")
            }
            patternId?.let {
                add("patternId=$it")
            }
            order?.let {
                add("order=$it")
            }
        }

        return if (params.isEmpty()) {
            "learning_steps/patterns/add"
        } else {
            "learning_steps/patterns/add?${params.joinToString("&")}"
        }
    }

    fun learningStepsPatternConfig(patternId: Long, configId: Long? = null, order: Int? = null): String {
        val params = buildList {
            configId?.let {
                add("configId=$it")
            }
            order?.let {
                add("order=$it")
            }
        }

        return if (params.isEmpty()) {
            "learning_steps/pattern/config/$patternId"
        } else {
            "learning_steps/pattern/config/$patternId?${params.joinToString("&")}"
        }
    }
}

enum class ExitDestination {
    PREVIOUS,
    HOME
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.MAIN
    ) {
        composable(NavRoutes.MAIN) {
            MainContent(
                onGalleryClick = {navController.navigate(NavRoutes.GALLERY)},
                onLearningStepsClick = {navController.navigate(NavRoutes.LEARNING_STEPS_LIST)}
            )
        }

        composable(NavRoutes.GALLERY) {
            GalleryScreen(
                navController = navController,
                onBackClick = {navController.popBackStack()},
                onHomeClick = {navController.navigate(NavRoutes.MAIN)},
                onCreateClick = {navController.navigate(NavRoutes.PATTERN_CREATE)}
            )
        }

        composable(route = NavRoutes.PATTERN_CREATE) {
            CreatePatternScreen(
                navController = navController,
                onBackClick = {navController.popBackStack()},
                onHomeClick = {navController.navigate(NavRoutes.MAIN)},
                onSaveSuccess = { patternId ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("newPatternId", patternId)
                    navController.popBackStack()
                },
            )
        }

        composable(route = NavRoutes.LEARNING_STEPS_LIST) {
            LearningStepsListScreen(
                navController = navController,
                onBackClick = {navController.popBackStack()},
                onHomeClick = {navController.navigate(NavRoutes.MAIN)},
                onCreateClick = {navController.navigate(NavRoutes.LEARNING_STEPS_CREATE)}
            )
        }

        composable(route = NavRoutes.LEARNING_STEPS_CREATE) {
            LearningStepsSettingsScreen(
                navController = navController,
                stepId = null,
                onBackClick = {navController.popBackStack()},
                onHomeClick = {navController.navigate(NavRoutes.MAIN)},
            )
        }

//        composable(route = NavRoutes.LEARNING_STEPS_PATTERNS) {
//            LearningStepsPatternsScreen(
//                navController = navController,
//                stepId = null,
//                onBackClick = {navController.popBackStack()},
//                onHomeClick = {navController.navigate(NavRoutes.MAIN)},
//                onAddPatternClick = { order ->
//                    navController.navigate(
//                        NavRoutes.learningStepsPatternAdd(
//                            order = order
//                        )
//                    )
//                }
//            )
//        }

        composable(
            route = NavRoutes.LEARNING_STEPS_PATTERN_ADD,
            arguments = listOf(
                navArgument("configId") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument("patternId") {
                    type = NavType.LongType
                    defaultValue = -1L
                },
                navArgument("order") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val configId = backStackEntry.arguments?.getLong("configId")?.takeIf { it != -1L }
            val patternId = backStackEntry.arguments?.getLong("patternId")?.takeIf { it != -1L }
            val order = backStackEntry.arguments?.getInt("order")?.takeIf { it != -1 }?: 0

            LearningStepsPatternAddScreen(
                navController = navController,
                configId = configId,
                selectedPatternId = patternId,
                order = order,
                onBackClick = {navController.popBackStack()},
                onHomeClick = {navController.navigate(NavRoutes.MAIN)},
                onNextClick = { selectedPatternId ->
                    navController.navigate(
                        NavRoutes.learningStepsPatternConfig(
                            patternId = selectedPatternId,
                            configId = configId,
                            order = order
                        )
                    )
                }
            )
        }

        composable(
            route = NavRoutes.LEARNING_STEPS_PATTERN_CONFIG,
            arguments = listOf(
                navArgument("patternId") {
                    type = NavType.LongType
                },
                navArgument("configId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("order") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) { backStackEntry ->
            val patternId = backStackEntry.arguments?.getLong("patternId") ?: return@composable
            val configId = backStackEntry.arguments?.getString("configId")?.toLongOrNull()
            val order = backStackEntry.arguments?.getInt("order")?: 0

            LearningStepPatternConfigScreen(
                navController = navController,
                patternId = patternId,
                configId = configId,
                order = order,
                onBackClick = {navController.popBackStack()},
                onHomeClick = {navController.navigate(NavRoutes.MAIN)},
                onSaveClick = { newConfigId ->
                    navController.getBackStackEntry(
                        NavRoutes.LEARNING_STEPS_CREATE
                    )
                        .savedStateHandle
                        .set("newConfigId", newConfigId)
                    navController.popBackStack(
                        route = NavRoutes.LEARNING_STEPS_CREATE,
                        inclusive = false
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
//    activeConfiguration: Pair<String, String>?,
    onLearningStepsClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    Scaffold(
        containerColor = Primary50,
        topBar = {
            TemplateTopAppBar(
                text = stringResource(R.string.main_screen_header),
                onBackClick = {},
                onHomeClick = {},
                isMainScreen = true
            )
        }
    )  { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                //Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
//                    text = activeConfiguration?.let {
//                        "Aktywny krok: ${it.first} (tryb: ${it.second})"
//                    } ?: "Brak aktywnego kroku uczenia",
                        text = stringResource(R.string.active_learning_step),
                        fontSize = 24.sp,
                        color = Primary900,
                        textAlign = TextAlign.Left,
                        fontWeight = FontWeight.Normal
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column() {
                        TemplateButton(
                            modifier = Modifier
                                .width((LocalConfiguration.current.screenWidthDp * 0.3f).dp)
                                .height(54.dp),
                            enabled = true,
                            onClick = onGalleryClick,
                            text = stringResource(R.string.pattern_gallery_button_text),
                            icon = Icons.Default.PhotoLibrary
                        )

                        Spacer(Modifier.height(10.dp))

                        TemplateButton(
                            modifier = Modifier
                                .width(((LocalConfiguration.current.screenWidthDp * 0.3f).dp))
                                .height(54.dp),
                            enabled = true,
                            onClick = onLearningStepsClick,
                            text = stringResource(R.string.learning_steps_button_text),
                            icon = Icons.Default.AssignmentTurnedIn
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(15.dp),
                        color = Primary300,
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                    ) {
                        Text(
                            text = stringResource(R.string.main_screen_info),
                            modifier = Modifier.padding(10.dp),
                            textAlign = TextAlign.Center,
                            color = Primary1000,
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.7.sp
                        )
                    }

                    Spacer(Modifier.width(12.dp))

                    Image(
                        painter = painterResource(R.drawable.rybka),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.55f)
                            .aspectRatio(1f)
                    )
                }
            }
        }
    }
}