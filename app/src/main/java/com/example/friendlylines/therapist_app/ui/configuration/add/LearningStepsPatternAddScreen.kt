package com.example.friendlylines.therapist_app.ui.configuration.add

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.composables.core.rememberScrollAreaState
import com.example.friendlylines.R
import com.example.friendlylines.components.LoadingScreen
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.backgrounds.PlainBackground
import com.example.friendlylines.therapist_app.navigation.ExitDestination
import com.example.friendlylines.therapist_app.navigation.TemplateTopAppBar
import com.example.friendlylines.therapist_app.ui.components.TemplateAlertDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.materials.components.PatternListArea
import com.example.friendlylines.therapist_app.ui.materials.gallery.GalleryScreenViewModel
import com.example.shared.data.models.PatternItem

private val CONTENT_PADDING = 20.dp

@Composable
fun LearningStepsPatternAddScreen(
    navController: NavController,
    stepId: Long?,
    configId: Long?,
    selectedPatternId: Long?,
    order: Int?,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onNextClick: (Long) -> Unit
) {
    val patternNumber = (order ?: 0) + 1

    val galleryViewModel: GalleryScreenViewModel = hiltViewModel()

    val gridState = rememberLazyGridState()
    val scrollAreaState = rememberScrollAreaState(gridState)

    val galleryState by galleryViewModel.state.collectAsStateWithLifecycle()

    var selectedPattern by remember {
        mutableStateOf<PatternItem?>(null)
    }

    LaunchedEffect(galleryState.patterns, selectedPatternId) {
        if (selectedPatternId != null) {
            selectedPattern = galleryState.patterns.find {
                it.pattern.id == selectedPatternId
            }
        }
    }

    var showExitDialog by remember {
        mutableStateOf(false)
    }

    var exitDestination by remember {
        mutableStateOf<ExitDestination?>(null)
    }

    BackHandler {
        showExitDialog = true
        exitDestination = ExitDestination.PREVIOUS
    }

    Scaffold(
        containerColor = FriendlyLinesColors.PrimaryFriendlyLines.P50,
        topBar = {
            TemplateTopAppBar(
                text = stringResource(R.string.pattern_text) + " " + "$patternNumber",
                onBackClick = {
                    showExitDialog = true
                    exitDestination = ExitDestination.PREVIOUS
                },
                onHomeClick = {
                    showExitDialog = true
                    exitDestination = ExitDestination.HOME
                },
                infoTitle = stringResource(R.string.info_dialog_title),
                infoMessage = stringResource(R.string.learning_steps_pattern_add_screen_info)
            )
        }
    ) { padding ->
        val content: @Composable () -> Unit = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(CONTENT_PADDING.scaled()),
                verticalArrangement = Arrangement.spacedBy(32.dp.scaled())
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(R.string.select_pattern_text),
                        style = FriendlyLinesTextStyles.bodyRegular,
                        color = FriendlyLinesColors.PrimaryFriendlyLines.P900
                    )
                }

                PatternListArea(
                    patterns = galleryState.patterns,
                    gridState = gridState,
                    scrollAreaState = scrollAreaState,
                    onCreateClick = {},
                    onDeleteClick = {},
                    onPatternClick = { pattern ->
                        selectedPattern = pattern
                    },
                    showAddPatternItem = false,
                    showBorder = true,
                    showDeleteButton = false,
                    isPatternSelected = { pattern ->
                        selectedPattern?.pattern?.id == pattern.pattern.id
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(1f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.selected_pattern_text) + " " + (selectedPattern?.pattern?.name ?: "-"),
                        style = FriendlyLinesTextStyles.bodyRegular,
                        color = FriendlyLinesColors.PrimaryFriendlyLines.P900
                    )

                    TemplateButton(
                        enabled = selectedPattern != null,
                        onClick = {
                            selectedPattern?.let {
                                onNextClick(it.pattern.id)
                            }
                        },
                        text = stringResource(R.string.next_button_text),
                        icon = Icons.Filled.ArrowForwardIos
                    )
                }
            }
        }

        if (galleryState.isLoading) {
            PlainBackground(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
            ) { LoadingScreen() }
        } else {
            PlainBackground(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
            ) { content() }
        }
    }

    if (showExitDialog) {
        TemplateAlertDialog(
            title = stringResource(R.string.exit_dialog_title),
            message = stringResource(R.string.exit_dialog_message),
            confirmText = stringResource(R.string.exit_confirm_button_text),
            dismissText = stringResource(R.string.dismiss_button_text),
            onConfirm = {
                showExitDialog = false
                when (exitDestination) {
                    ExitDestination.PREVIOUS -> {
                        onBackClick()
                    }
                    ExitDestination.HOME -> {
                        onHomeClick()
                    }
                    null -> Unit
                }
                exitDestination = null
            },
            onDismiss = {
                showExitDialog = false
                exitDestination = null
            }
        )
    }
}