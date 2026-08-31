package com.example.friendlylines.therapist_app.ui.configuration.patterns

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.composables.core.ScrollArea
import com.composables.core.Thumb
import com.composables.core.VerticalScrollbar
import com.composables.core.rememberScrollAreaState
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.components.LearningStepsPatternItem
import com.example.friendlylines.therapist_app.ui.components.TemplateAlertDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateSearchBox
import com.example.friendlylines.therapist_app.ui.configuration.config.LearningStepsPatternConfigDraft
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepTab
import com.example.friendlylines.therapist_app.ui.main.NavRoutes
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900

enum class MoveDirection {
    UP,
    DOWN
}

@Composable
fun LearningStepsPatternsScreen(
    navController: NavController,
    stepId: Long?,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onAddPatternClick: (Int) -> Unit,
    onNextClick: () -> Unit,
) {
    val listState = rememberLazyListState()
    val scrollAreaState = rememberScrollAreaState(listState)

    var scrollToConfigId by remember {
        mutableStateOf<Long?>(null)
    }

    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(
            NavRoutes.LEARNING_STEPS_CREATE
        )
    }

    val newConfigId by parentEntry
        .savedStateHandle
        .getStateFlow<Long?>("newConfigId", null)
        .collectAsStateWithLifecycle()

    val viewModel: LearningStepsPatternsScreenViewModel = hiltViewModel()
    val patterns by viewModel.patternsDraft.collectAsStateWithLifecycle()

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

    LaunchedEffect(patterns, newConfigId) {
        val configId = newConfigId ?: return@LaunchedEffect

        val index = patterns.indexOfFirst {
            it.id == configId
        }

        if (index >= 0) {
            listState.animateScrollToItem(index)

            parentEntry
                .savedStateHandle
                .set<Long?>("newConfigId", null)
        }
    }

    LaunchedEffect(scrollToConfigId, patterns) {
        val configId = scrollToConfigId ?: return@LaunchedEffect

        val index = patterns.indexOfFirst {
            it.id == configId
        }

        if (index >= 0) {
            listState.animateScrollToItem(index)
            scrollToConfigId = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            //.padding(padding)
            .background(Primary50)
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
            thickness = 1.dp,
            color = Primary700
        )

        LearningStepsPatternsListHeader()

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
                            horizontal = 32.dp,
                            vertical = 16.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(
                            items = filteredPatterns,
                            key = { _, pattern -> pattern.id }
                        ) { index, pattern ->
                            LearningStepsPatternItem(
                                index = index,
                                pattern = pattern,
                                canMoveUp = index > 0,
                                canMoveDown = index < patterns.lastIndex,
                                onEnabledChange = { enabled ->
                                    viewModel.setPatternEnabled(
                                        configId = pattern.id,
                                        enabled = enabled
                                    )
                                },
                                onMoveUpClick = {
                                    viewModel.movePattern(
                                        configId = pattern.id,
                                        direction = MoveDirection.UP
                                    )
                                },
                                onMoveDownClick = {
                                    viewModel.movePattern(
                                        configId = pattern.id,
                                        direction = MoveDirection.DOWN
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

    if (patternConfigToEdit != null) {
        TemplateAlertDialog(
            title = stringResource(R.string.edit_dialog_title),
            message = stringResource(R.string.edit_dialog_message),
            confirmText = stringResource(R.string.edit_button_text),
            dismissText = stringResource(R.string.dismiss_button_text),
            onConfirm = {
                patternConfigToEdit?.let { pattern ->
                    navController.navigate(
                        NavRoutes.learningStepsPatternAdd(
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
                    val newConfigId = viewModel.copyPattern(pattern.id)
                    if (newConfigId != null) {
                        scrollToConfigId = newConfigId
                    }
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
                    viewModel.deletePattern(pattern.id)
                }
                patternConfigToDelete = null
            },
            onDismiss = {
                patternConfigToDelete = null
            }
        )
    }
}

@Composable
fun LearningStepsPatternsToolbar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onAddPatternClick: () -> Unit,
    onNextClick: () -> Unit,
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
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.weight(1f))

        TemplateButton(
            modifier = Modifier
                .wrapContentWidth()
                .height(54.dp),
            enabled = true,
            onClick = onAddPatternClick,
            text = stringResource(R.string.add_pattern_button_text),
            icon = Icons.Default.AddCircle
        )

        Spacer(modifier = Modifier.width(32.dp))

        TemplateButton(
            modifier = Modifier
                .wrapContentWidth()
                .height(54.dp),
            enabled = true,
            onClick = onNextClick,
            text = stringResource(R.string.next_button_text),
            icon = Icons.Outlined.ArrowForwardIos
        )
    }
}

@Composable
fun LearningStepsPatternsListHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 64.dp,
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
                text = stringResource(R.string.pattern_list_header_name),
                fontSize = 16.7.sp,
                fontWeight = FontWeight.Normal,
                color = Primary900
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