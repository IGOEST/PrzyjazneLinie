package com.example.friendlylines.therapist_app.ui.configuration.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.composables.core.ScrollArea
import com.composables.core.Thumb
import com.composables.core.VerticalScrollbar
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.components.LearningStepsPatternItem
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateCheckbox
import com.example.friendlylines.therapist_app.ui.components.TemplateClickableIcon
import com.example.friendlylines.therapist_app.ui.components.TemplateInfoDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateSearchBox
import com.example.friendlylines.therapist_app.ui.components.TemplateTopAppBar
import com.example.friendlylines.therapist_app.ui.configuration.patterns.LearningStepsPatternsEvent
import com.example.friendlylines.therapist_app.ui.configuration.patterns.MoveDirection
import com.example.friendlylines.therapist_app.ui.theme.InfoActive
import com.example.friendlylines.therapist_app.ui.theme.InfoDefault
import com.example.friendlylines.therapist_app.ui.theme.Neutral300
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import com.composables.core.rememberScrollAreaState
import com.example.friendlylines.therapist_app.ui.components.LearningStepsListItem
import com.example.friendlylines.therapist_app.ui.components.LearningStepsListItemAdd
import com.example.friendlylines.therapist_app.ui.components.TemplateAlertDialog
import com.example.friendlylines.therapist_app.ui.main.NavRoutes
import com.example.shared.data.drafts.LearningStepsPatternConfigDraft
import com.example.shared.data.entities.LearningStepEntity

@Composable
fun LearningStepsListScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onCreateClick: () -> Unit,
    viewModel: LearningStepsListScreenViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val scrollAreaState = rememberScrollAreaState(listState)

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadLearningSteps()
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

    val filteredLearningSteps = remember(state.learningSteps, state.searchQuery) {
        if (state.searchQuery.isBlank()) {
            state.learningSteps
        } else {
            state.learningSteps.filter {
                it.name.contains(
                    state.searchQuery.trim(),
                    ignoreCase = true
                )
            }
        }
    }

    var showInfoDialog by remember {
        mutableStateOf(false)
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

    Scaffold(
        containerColor = Primary50,
        topBar = {
            TemplateTopAppBar(
                text = stringResource(R.string.learning_steps_header),
                onBackClick = onBackClick,
                onHomeClick = onHomeClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LearningStepsListToolbar(
                searchQuery = state.searchQuery,
                onSearchQueryChange = {
                    viewModel.onEvent(
                        LearningStepsListEvent.SearchQueryChanged(it)
                    )
                },
                onCreateClick = onCreateClick,
                onInfoClick = {
                    showInfoDialog = true
                }
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = Primary700
            )

            LearningStepsListHeader(
                hideExampleSteps = hideExampleSteps,
                onHideExampleStepsChange = {
                    hideExampleSteps = it
                }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 32.dp
                    )
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
                                vertical = 16.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                    onModeChange = { isLearning ->
                                        viewModel.onEvent(
                                            LearningStepsListEvent.ModeChanged(
                                                id = learningStep.id,
                                                isLearning = isLearning
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

                        Spacer(modifier = Modifier.width(8.dp))

                        VerticalScrollbar(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(4.dp)
                        ) {
                            Thumb(
                                Modifier.background(
                                    color = Primary700,
                                    shape = RoundedCornerShape(2.dp)
                                )
                            )
                        }
                    }
                }
            }
        }

        if (showInfoDialog) {
            TemplateInfoDialog(
                title = stringResource(R.string.info_dialog_title),
                info = stringResource(R.string.learning_steps_info),
                onDismiss = {
                    showInfoDialog = false
                }
            )
        }

        if (learningStepToEdit != null) {
            TemplateAlertDialog(
                title = stringResource(R.string.edit_dialog_title),
                message = stringResource(R.string.edit_dialog_message),
                confirmText = stringResource(R.string.edit_button_text),
                dismissText = stringResource(R.string.dismiss_button_text),
                onConfirm = {
                    learningStepToEdit?.let { learningStep ->
                        navController.navigate(
                            NavRoutes.learningStepsCreate(
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
                message = stringResource(R.string.copy_dialog_message),
                confirmText = stringResource(R.string.copy_button_text),
                dismissText = stringResource(R.string.dismiss_button_text),
                onConfirm = {
                    learningStepToCopy?.let { learningStep ->
                        viewModel.onEvent(
                            LearningStepsListEvent.CopyClicked(learningStep.id)
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
                message = stringResource(R.string.delete_pattern_config_dialog_message),
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
    }
}

@Composable
fun LearningStepsListToolbar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCreateClick: () -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TemplateSearchBox (
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = stringResource(R.string.search_box_text),
            modifier = Modifier.weight(0.8f)
        )

        Spacer(modifier = Modifier.width(24.dp))

        TemplateClickableIcon(
            icon = Icons.Default.Info,
            contentDescription = null,
            onClick = {
                onInfoClick()
            },
            modifier = Modifier.size(24.dp),
            defaultTint = InfoDefault,
            activeTint = InfoActive,
            disabledTint = Neutral300
        )

        Spacer(modifier = Modifier.weight(1f))

        TemplateButton(
            modifier = Modifier
                .width(170.dp)
                .height(54.dp),
            enabled = true,
            onClick = onCreateClick,
            text = stringResource(R.string.create_button_text),
            icon = Icons.Outlined.AddCircle
        )
    }
}

@Composable
fun LearningStepsListHeader(
    hideExampleSteps: Boolean,
    onHideExampleStepsChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 32.dp,
                end = 96.dp,
                top = 16.dp,
                bottom = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(R.string.steps_list_header_name),
                fontSize = 16.7.sp,
                fontWeight = FontWeight.Normal,
                color = Primary900
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.hide_exemplary_steps),
                fontSize = 16.7.sp,
                fontWeight = FontWeight.Normal,
                color = Primary900
            )

            Spacer(modifier = Modifier.width(8.dp))

            TemplateCheckbox(
                checked = hideExampleSteps,
                onCheckedChange = onHideExampleStepsChange,
                modifier = Modifier.size(18.dp)
            )
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = stringResource(R.string.list_header_actions),
                fontSize = 16.7.sp,
                fontWeight = FontWeight.Normal,
                color = Primary900
            )
        }
    }
}