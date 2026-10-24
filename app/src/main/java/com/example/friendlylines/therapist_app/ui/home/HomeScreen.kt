package com.example.friendlylines.therapist_app.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.friendlylines.R
import com.example.friendlylines.name_mapping.ExampleStepsNames
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.backgrounds.HomeBackground
import com.example.friendlylines.therapist_app.ui.components.InfoIconButton
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.navigation.TemplateTopAppBar

@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onLearningStepsClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
) {
    val activeLearningStep by viewModel.activeLearningStep.collectAsStateWithLifecycle()

    BackHandler { onBackClick() }

    val navController = rememberNavController()

    LaunchedEffect(navController.currentBackStackEntryAsState().value) {
        viewModel.loadActiveLearningStep()
    }

    Scaffold(
        containerColor = FriendlyLinesColors.PrimaryFriendlyLines.P50,
        topBar = {
            TemplateTopAppBar(
                text = stringResource(R.string.home_screen_header),
                onBackClick = onBackClick,
                onHomeClick = onHomeClick,
                showHomeIcon = false,
                infoTitle = stringResource(R.string.info_dialog_title),
                infoMessage = stringResource(R.string.home_screen_info_message)
            )
        }
    )  { padding ->
        HomeBackground {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp.scaled()),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(
                                bottom = 32.dp.scaled()
                            ),
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Text(
                            text = activeLearningStep?.let { step ->
                                val stepName = if (step.isExample) {
                                    ExampleStepsNames.exampleLearningStepNameFor(step.name)
                                } else {
                                    step.name
                                }

                                stringResource(R.string.active_learning_step) + " $stepName" + " (${if (step.isTest) stringResource(R.string.test_mode) else stringResource(R.string.learning_mode)})"
                            } ?: stringResource(R.string.no_active_step),
                            style = FriendlyLinesTextStyles.headingH5Regular,
                            color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                            textAlign = TextAlign.Left
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp.scaled())
                    ) {
                        TemplateButton(
                            modifier = Modifier.width(346.dp.scaled()),
                            enabled = true,
                            onClick = onGalleryClick,
                            text = stringResource(R.string.pattern_gallery_button_text),
                            icon = Icons.Filled.Image
                        )

                        InfoIconButton(
                            infoTitle = stringResource(R.string.info_dialog_title),
                            infoMessage = stringResource(R.string.therapist_home_gallery_info_meassage),
                            defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                            activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                            disabledTint = FriendlyLinesColors.Neutral.N300
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp.scaled())
                    ) {
                        TemplateButton(
                            modifier = Modifier.width(346.dp.scaled()),
                            enabled = true,
                            onClick = onLearningStepsClick,
                            text = stringResource(R.string.learning_steps_button_text),
                            icon = Icons.Filled.Inventory
                        )

                        InfoIconButton(
                            infoTitle = stringResource(R.string.info_dialog_title),
                            infoMessage = stringResource(R.string.therapist_home_learning_steps_info_message),
                            defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                            activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                            disabledTint = FriendlyLinesColors.Neutral.N300
                        )
                    }
                }
            }
        }
    }
}