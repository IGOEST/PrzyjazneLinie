package com.example.friendlylines.therapist_app.ui.configuration.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.composables.core.ScrollArea
import com.composables.core.Thumb
import com.composables.core.VerticalScrollbar
import com.composables.core.rememberScrollAreaState
import com.example.friendlylines.R
import com.example.friendlylines.components.LoadingScreen
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.backgrounds.PlainBackground
import com.example.friendlylines.therapist_app.navigation.TherapistNavRoutes
import com.example.friendlylines.therapist_app.ui.components.TemplateAlertDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateInfoDialog
import com.example.friendlylines.therapist_app.navigation.TemplateTopAppBar
import com.example.friendlylines.therapist_app.ui.configuration.list.components.LearningStepsListHeader
import com.example.friendlylines.therapist_app.ui.configuration.list.components.LearningStepsListItem
import com.example.friendlylines.therapist_app.ui.configuration.list.components.LearningStepsListItemAdd
import com.example.friendlylines.therapist_app.ui.configuration.list.components.LearningStepsListToolbar
import com.example.friendlylines.therapist_app.ui.materials.components.SCROLLBAR_SHAPE
import com.example.friendlylines.therapist_app.ui.materials.components.SCROLLBAR_WIDTH
import com.example.shared.data.entities.LearningStepEntity

private val CONTENT_PADDING = 20.dp

@Composable
fun LearningStepsListScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onCreateClick: () -> Unit,
    onPlayClick: () -> Unit,
    viewModel: LearningStepsListScreenViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val scrollAreaState = rememberScrollAreaState(listState)

    val state by viewModel.state.collectAsStateWithLifecycle()

    val copySuffix = stringResource(R.string.copy_text)

    LaunchedEffect(Unit) {
        val newLearningStepId =
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.get<Long>("newLearningStepId")

        if (newLearningStepId != null) {
            viewModel.loadLearningSteps()
        }
    }

    LaunchedEffect(state.learningSteps) {
        val newLearningStepId =
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.get<Long>("newLearningStepId")

        if (newLearningStepId != null) {
            val index = state.learningSteps.indexOfFirst {
                it.id == newLearningStepId
            }

            if (index >= 0) {
                listState.animateScrollToItem(index)

                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<Long>("newLearningStepId")
            }
        }
    }

    LaunchedEffect(state.learningSteps, state.copiedLearningStepId) {
        val copiedId = state.copiedLearningStepId ?: return@LaunchedEffect

        val index = state.learningSteps.indexOfFirst {
            it.id == copiedId
        }

        if (index >= 0) {
            listState.animateScrollToItem(index)

            viewModel.onEvent(
                LearningStepsListEvent.CopiedLearningStepHandled
            )
        }
    }

    var hideExampleSteps by remember {
        mutableStateOf(false)
    }

    var learningStepToEdit by remember {
        mutableStateOf<LearningStepEntity?>(null)
    }

    var learningStepToCopy by remember {
        mutableStateOf<LearningStepEntity?>(null)
    }

    var learningStepToDelete by remember {
        mutableStateOf<LearningStepEntity?>(null)
    }

    val canPlay by viewModel.canPlay.collectAsStateWithLifecycle()

    var showCannotPlayInfo by remember {
        mutableStateOf(false)
    }

    val filteredLearningSteps = remember(state.learningSteps, state.searchQuery, hideExampleSteps) {
        val steps = if (state.searchQuery.isBlank()) {
            state.learningSteps
        } else {
            state.learningSteps.filter {
                it.name.contains(
                    state.searchQuery.trim(),
                    ignoreCase = true
                )
            }
        }

        if (hideExampleSteps) {
            steps.filter { !it.isExample }
        } else { steps
        }
    }

    BackHandler {
        onBackClick()
    }

    Scaffold(
        containerColor = FriendlyLinesColors.PrimaryFriendlyLines.P50,
        topBar = {
            TemplateTopAppBar(
                text = stringResource(R.string.learning_steps_header),
                onBackClick = onBackClick,
                onHomeClick = onHomeClick,
                infoTitle = stringResource(R.string.info_dialog_title),
                infoMessage = stringResource(R.string.learning_steps_list_screen_info_message)
            )
        }
    ) { padding ->
        val content: @Composable () -> Unit = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(CONTENT_PADDING.scaled()),
                verticalArrangement = Arrangement.spacedBy(22.dp.scaled()),
            ) {
                LearningStepsListToolbar(
                    searchQuery = state.searchQuery,
                    onSearchQueryChange = {
                        viewModel.onEvent(
                            LearningStepsListEvent.SearchQueryChanged(it)
                        )
                    },
                    onCreateClick = onCreateClick,
                    onPlayClick = {
                        if (canPlay) onPlayClick()
                        else showCannotPlayInfo = true
                    },
                    hideExampleSteps = hideExampleSteps,
                    onHideExampleStepsChange = {
                        hideExampleSteps = it
                    }
                )

                HorizontalDivider(
                    thickness = 3.dp.scaled(),
                    color = FriendlyLinesColors.PrimaryFriendlyLines.P700
                )

                LearningStepsListHeader()

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ScrollArea(
                        state = scrollAreaState,
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(
                                    vertical = 16.dp.scaled()
                                ),
                                verticalArrangement = Arrangement.spacedBy(8.dp.scaled())
                            ) {
                                items(
                                    items = filteredLearningSteps,
                                    key = { it.id }
                                ) { learningStep ->
                                    LearningStepsListItem(
                                        learningStep = learningStep,
                                        onEnabledChange = { isActive ->
                                            viewModel.onEvent(
                                                LearningStepsListEvent.ActiveChanged(
                                                    id = learningStep.id,
                                                    isActive = isActive
                                                )
                                            )
                                        },
                                        onModeChange = { isTest ->
                                            viewModel.onEvent(
                                                LearningStepsListEvent.ModeChanged(
                                                    id = learningStep.id,
                                                    isTest = isTest
                                                )
                                            )
                                        },
                                        onEditClick = {
                                            learningStepToEdit = learningStep
                                        },
                                        onCopyClick = {
                                            learningStepToCopy = learningStep
                                        },
                                        onDeleteClick = {
                                            learningStepToDelete = learningStep
                                        }
                                    )
                                }

                                item {
                                    LearningStepsListItemAdd(
                                        onClick = onCreateClick
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp.scaled()))

                            VerticalScrollbar(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(SCROLLBAR_WIDTH.scaled())
                            ) {
                                Thumb(
                                    Modifier.background(
                                        color = FriendlyLinesColors.PrimaryFriendlyLines.P700,
                                        shape = RoundedCornerShape(SCROLLBAR_SHAPE.scaled())
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        if (state.isLoading) {
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

        if (learningStepToEdit != null) {
            TemplateAlertDialog(
                title = stringResource(R.string.edit_dialog_title),
                message = stringResource(R.string.edit_learning_step_dialog_message),
                confirmText = stringResource(R.string.edit_button_text),
                dismissText = stringResource(R.string.dismiss_button_text),
                onConfirm = {
                    learningStepToEdit?.let { learningStep ->
                        navController.navigate(
                            TherapistNavRoutes.LearningStepsCreate(
                                stepId = learningStep.id
                            )
                        )
                    }
                    learningStepToEdit = null
                },
                onDismiss = {
                    learningStepToEdit = null
                }
            )
        }

        if (learningStepToCopy != null) {
            TemplateAlertDialog(
                title = stringResource(R.string.copy_dialog_title),
                message = stringResource(R.string.copy_learning_step_dialog_message),
                confirmText = stringResource(R.string.copy_button_text),
                dismissText = stringResource(R.string.dismiss_button_text),
                onConfirm = {
                    learningStepToCopy?.let { learningStep ->
                        viewModel.onEvent(
                            LearningStepsListEvent.CopyClicked(
                                id = learningStep.id,
                                copySuffix = copySuffix)
                        )
                    }
                    learningStepToCopy = null
                },
                onDismiss = {
                    learningStepToCopy = null
                }
            )
        }

        if (learningStepToDelete != null) {
            TemplateAlertDialog(
                title = stringResource(R.string.delete_dialog_title),
                message = stringResource(R.string.delete_learning_step_dialog_message),
                confirmText = stringResource(R.string.delete_confirm_button_text),
                dismissText = stringResource(R.string.dismiss_button_text),
                onConfirm = {
                    learningStepToDelete?.let { learningStep ->
                        viewModel.onEvent(
                            LearningStepsListEvent.DeleteClicked(learningStep.id)
                        )
                    }
                    learningStepToDelete = null
                },
                onDismiss = {
                    learningStepToDelete = null
                }
            )
        }

        if (showCannotPlayInfo) {
            TemplateInfoDialog(
                title = stringResource(R.string.cannot_play_dialog_title),
                message = stringResource(R.string.cannot_play_dialog_message),
                onDismiss = {
                    showCannotPlayInfo = false
                }
            )
        }
    }
}