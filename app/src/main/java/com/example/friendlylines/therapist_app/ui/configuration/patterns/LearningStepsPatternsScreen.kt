package com.example.friendlylines.therapist_app.ui.configuration.patterns

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.composables.core.ScrollArea
import com.composables.core.Thumb
import com.composables.core.VerticalScrollbar
import com.composables.core.rememberScrollAreaState
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.navigation.TherapistNavRoutes
import com.example.friendlylines.therapist_app.ui.components.TemplateAlertDialog
import com.example.friendlylines.therapist_app.ui.configuration.patterns.components.LearningStepsPatternListItem
import com.example.friendlylines.therapist_app.ui.configuration.patterns.components.LearningStepsPatternsListHeader
import com.example.friendlylines.therapist_app.ui.configuration.patterns.components.LearningStepsPatternsToolbar
import com.example.friendlylines.therapist_app.ui.materials.components.SCROLLBAR_SHAPE
import com.example.friendlylines.therapist_app.ui.materials.components.SCROLLBAR_WIDTH
import com.example.shared.data.drafts.LearningStepsPatternConfigDraft
import com.example.shared.data.drafts.LearningStepsPatternsDraft

enum class MoveDirection {
    UP,
    DOWN
}

private val CONTENT_PADDING = 20.dp

@Composable
fun LearningStepsPatternsScreen(
    navController: NavController,
    stepId: Long?,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onAddPatternClick: (Int) -> Unit,
    onNextClick: () -> Unit,
    onEvent: (LearningStepsPatternsEvent)    -> Unit,
    draft: LearningStepsPatternsDraft
) {
    val patterns = draft.patterns
    val scrollToConfigId = draft.scrollToConfigId

    val listState = rememberLazyListState()
    val scrollAreaState = rememberScrollAreaState(listState)

    var searchQuery by remember {
        mutableStateOf("")
    }

    val filteredPatterns = remember(patterns, searchQuery) {
        if (searchQuery.isBlank()) {
            patterns
        } else {
            patterns.filter { config ->
                config.pattern.pattern.name.contains(
                    searchQuery.trim(),
                    ignoreCase = true
                )
            }
        }
    }

    var patternConfigToEdit by remember {
        mutableStateOf<LearningStepsPatternConfigDraft?>(null)
    }

    var patternConfigToCopy by remember {
        mutableStateOf<LearningStepsPatternConfigDraft?>(null)
    }

    var patternConfigToDelete by remember {
        mutableStateOf<LearningStepsPatternConfigDraft?>(null)
    }

    LaunchedEffect(scrollToConfigId, patterns) {
        val configId = scrollToConfigId ?: return@LaunchedEffect

        val index = patterns.indexOfFirst {
            it.id == configId
        }

        if (index >= 0) {
            listState.animateScrollToItem(index)
            onEvent(
                LearningStepsPatternsEvent.ScrollToConfigHandled
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(CONTENT_PADDING.scaled()),
        verticalArrangement = Arrangement.spacedBy(22.dp.scaled()),
    ) {
        LearningStepsPatternsToolbar(
            searchQuery = searchQuery,
            onSearchQueryChange = {
                searchQuery = it
            },
            onAddPatternClick = {
                val newPatternIndex = patterns.size
                onAddPatternClick(newPatternIndex)
            },
            onNextClick = onNextClick
        )

        HorizontalDivider(
            thickness = 3.dp.scaled(),
            color = FriendlyLinesColors.PrimaryFriendlyLines.P700
        )

        LearningStepsPatternsListHeader()

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
                            vertical = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(
                            items = filteredPatterns,
                            key = { _, pattern -> pattern.id }
                        ) { index, pattern ->
                            LearningStepsPatternListItem(
                                index = index,
                                pattern = pattern,
                                canMoveUp = index > 0,
                                canMoveDown = index < patterns.lastIndex,
                                onEnabledChange = { enabled ->
                                    onEvent(
                                        LearningStepsPatternsEvent.SetPatternEnabled(
                                            configId = pattern.id,
                                            enabled = enabled
                                        )
                                    )
                                },
                                onLearningToggled = { isLearning ->
                                    onEvent(
                                        LearningStepsPatternsEvent.SetPatternLearning(
                                            configId = pattern.id,
                                            isLearning = isLearning
                                        )
                                    )
                                },
                                onTestToggled = { isTest ->
                                    onEvent(
                                        LearningStepsPatternsEvent.SetPatternTest(
                                            configId = pattern.id,
                                            isTest = isTest
                                        )
                                    )
                                },
                                onMoveUpClick = {
                                    onEvent(
                                        LearningStepsPatternsEvent.MovePattern(
                                            configId = pattern.id,
                                            direction = MoveDirection.UP
                                        )
                                    )
                                },
                                onMoveDownClick = {
                                    onEvent(
                                        LearningStepsPatternsEvent.MovePattern(
                                            configId = pattern.id,
                                            direction = MoveDirection.DOWN
                                        )
                                    )
                                },
                                onEditClick = {
                                    patternConfigToEdit = pattern
                                },
                                onCopyClick = {
                                    patternConfigToCopy = pattern
                                },
                                onDeleteClick = {
                                    patternConfigToDelete = pattern
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

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

    if (patternConfigToEdit != null) {
        TemplateAlertDialog(
            title = stringResource(R.string.edit_dialog_title),
            message = stringResource(R.string.edit_dialog_message),
            confirmText = stringResource(R.string.edit_button_text),
            dismissText = stringResource(R.string.dismiss_button_text),
            onConfirm = {
                patternConfigToEdit?.let { pattern ->
                    navController.navigate(
                        TherapistNavRoutes.LearningStepsPatternAdd(
                            stepId = stepId,
                            configId = pattern.id,
                            patternId = pattern.pattern.pattern.id,
                            order = pattern.order
                        )
                    )
                }
                patternConfigToEdit = null
            },
            onDismiss = {
                patternConfigToEdit = null
            }
        )
    }

    if (patternConfigToCopy != null) {
        TemplateAlertDialog(
            title = stringResource(R.string.copy_dialog_title),
            message = stringResource(R.string.copy_dialog_message),
            confirmText = stringResource(R.string.copy_button_text),
            dismissText = stringResource(R.string.dismiss_button_text),
            onConfirm = {
                patternConfigToCopy?.let { pattern ->
                    onEvent(
                        LearningStepsPatternsEvent.CopyPattern(
                            configId = pattern.id
                        )
                    )
                }
                patternConfigToCopy = null
            },
            onDismiss = {
                patternConfigToCopy = null
            }
        )
    }

    if (patternConfigToDelete != null) {
        TemplateAlertDialog(
            title = stringResource(R.string.delete_dialog_title),
            message = stringResource(R.string.delete_pattern_config_dialog_message),
            confirmText = stringResource(R.string.delete_confirm_button_text),
            dismissText = stringResource(R.string.dismiss_button_text),
            onConfirm = {
                patternConfigToDelete?.let { pattern ->
                    onEvent(
                        LearningStepsPatternsEvent.DeletePattern(
                            configId = pattern.id
                        )
                    )
                }
                patternConfigToDelete = null
            },
            onDismiss = {
                patternConfigToDelete = null
            }
        )
    }
}