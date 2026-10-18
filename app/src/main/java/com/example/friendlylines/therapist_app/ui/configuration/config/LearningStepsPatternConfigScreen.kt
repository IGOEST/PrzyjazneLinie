package com.example.friendlylines.therapist_app.ui.configuration.config

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.navigation.ExitDestination
import com.example.friendlylines.therapist_app.navigation.TherapistNavRoutes
import com.example.friendlylines.therapist_app.ui.components.InfoIconButton
import com.example.friendlylines.therapist_app.ui.components.TemplateAlertDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateDropdown
import com.example.friendlylines.therapist_app.ui.components.TemplateSlider
import com.example.friendlylines.therapist_app.ui.components.TemplateToggleSwitch
import com.example.friendlylines.therapist_app.navigation.TemplateTopAppBar
import com.example.friendlylines.therapist_app.ui.configuration.patterns.LearningStepsPatternsEvent
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepsSettingsEvent
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepsSettingsViewModel
import com.example.friendlylines.therapist_app.ui.materials.create_new.componenets.PatternPreview
import com.example.shared.data.drafts.ColorOption
import com.example.shared.data.drafts.PatternWidth
import com.example.shared.data.drafts.toDp

private val CONTENT_PADDING = 20.dp

@Composable
fun LearningStepPatternConfigScreen(
    navController: NavController,
    patternId: Long,
    stepId: Long?,
    configId: Long? = null,
    order: Int,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSaveClick: (Long?) -> Unit
) {
    LaunchedEffect(order) {
        Log.d("PatternConfig", "ORDER = $order")
    }

    val viewModel: LearningStepsPatternConfigScreenViewModel = hiltViewModel()
    val pattern by viewModel.pattern.collectAsStateWithLifecycle()
    LaunchedEffect(patternId) {
        viewModel.loadPattern(patternId)
    }

    val parentEntry = remember(navController.currentBackStackEntry, stepId) {
        navController.getBackStackEntry(
            TherapistNavRoutes.LearningStepsCreate(
                stepId = stepId
            )
        )
    }
    val settingsViewModel: LearningStepsSettingsViewModel = hiltViewModel(parentEntry)
    val draft by settingsViewModel.state.collectAsStateWithLifecycle()

    val existingConfig = remember(draft, configId) {
        draft.patternState.patterns.firstOrNull {
            it.id == configId
        }
    }

    var patternColor by remember {
        mutableStateOf<ColorOption?>(null)
    }

    var writingColor by remember {
        mutableStateOf<ColorOption?>(null)
    }

    var backgroundColor by remember {
        mutableStateOf<ColorOption?>(null)
    }

    var patternVariety by remember {
        mutableStateOf(false)
    }

    val thicknessValues = listOf(
        stringResource(R.string.thin_label_text),
        stringResource(R.string.medium_label_text),
        stringResource(R.string.thick_label_text)
    )

    var selectedWidth by remember {
        mutableStateOf(PatternWidth.MEDIUM)
    }

    val strokeWidth = selectedWidth.toDp()

    val patternAndWritingColors = PatternConfigColors.patternAndWriting()
    val backgroundColors = PatternConfigColors.background()

    val colorNames = patternAndWritingColors
        .plus(backgroundColors)
        .associate { option ->
            option.key to PatternConfigColors.nameFor(option.key)
        }

    val availablePatternColors = patternAndWritingColors.filter { option ->
        option != writingColor
    }

    val availableWritingColors = patternAndWritingColors.filter { option ->
        option != patternColor
    }

    val availableBackgroundColors = backgroundColors

    var showSaveConfigDialog by remember {
        mutableStateOf(false)
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

    val leftListState = rememberLazyListState()

    LaunchedEffect(existingConfig?.id) {
        existingConfig?.let { config ->
            selectedWidth = config.width
            patternColor = config.patternColor
            writingColor = config.writingColor
            backgroundColor = config.backgroundColor
            patternVariety = config.patternVariety
        }
    }

    Scaffold(
        containerColor = FriendlyLinesColors.PrimaryFriendlyLines.P50,
        topBar = {
            TemplateTopAppBar(
                text = stringResource(R.string.pattern_text) + " " + "${order + 1}",
                onBackClick = {
                    showExitDialog = true
                    exitDestination = ExitDestination.PREVIOUS
                },
                onHomeClick = {
                    showExitDialog = true
                    exitDestination = ExitDestination.HOME
                },
                infoTitle = stringResource(R.string.info_dialog_title),
                infoMessage = stringResource(R.string.learning_steps_pattern_config_screen_info)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(CONTENT_PADDING.scaled())
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    LazyColumn(
                        state = leftListState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(32.dp.scaled()),
                        verticalArrangement = Arrangement.spacedBy(32.dp.scaled()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            TemplateSlider(
                                values = thicknessValues,
                                selectedIndex = selectedWidth.ordinal,
                                onValueSelected = { index ->
                                    selectedWidth = PatternWidth.entries[index]
                                },
                                showHeader = true,
                                showInfoIcon = true,
                                headerText = stringResource(R.string.pattern_thickness_text),
                                infoTitle = stringResource(R.string.pattern_thickness_text),
                                infoMessage = stringResource(R.string.pattern_thickness_info)
                            )
                        }

                        item {
                            TemplateDropdown(
                                value = patternColor,
                                options = availablePatternColors,
                                onOptionSelected = {
                                    patternColor = it
                                },
                                label = stringResource(R.string.pattern_color_text),
                                optionLabel = { option ->
                                    option?.let { colorNames[it.key] } ?: ""
                                },
                                showEmptyOption = true,
                                showHeader = true,
                                headerText = stringResource(R.string.pattern_color_text)
                            )
                        }

                        item {
                            TemplateDropdown(
                                value = writingColor,
                                options = availableWritingColors,
                                onOptionSelected = {
                                    writingColor = it
                                },
                                label = stringResource(R.string.drawing_color_text),
                                optionLabel = { option ->
                                    option?.let { colorNames[it.key] } ?: ""
                                },
                                showEmptyOption = true,
                                showHeader = true,
                                headerText = stringResource(R.string.drawing_color_text)
                            )
                        }

                        item {
                            TemplateDropdown(
                                value = backgroundColor,
                                options = availableBackgroundColors,
                                onOptionSelected = {
                                    backgroundColor = it
                                },
                                label = stringResource(R.string.background_color_text),
                                optionLabel = { option ->
                                    option?.let { colorNames[it.key] } ?: ""
                                },
                                showEmptyOption = true,
                                showHeader = true,
                                headerText = stringResource(R.string.background_color_text)
                            )
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp.scaled()),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    InfoIconButton(
                                        infoTitle = stringResource(R.string.pattern_variety_text),
                                        infoMessage = stringResource(R.string.pattern_variety_info),
                                        modifier = Modifier.size(24.dp.scaled()),
                                        defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                                        activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                                        disabledTint = FriendlyLinesColors.Neutral.N300
                                    )
                                    Text(
                                        text = stringResource(R.string.pattern_variety_text),
                                        style = FriendlyLinesTextStyles.headingH5Regular,
                                        color = FriendlyLinesColors.PrimaryFriendlyLines.P900
                                    )
                                }

                                TemplateToggleSwitch(
                                    checked = patternVariety,
                                    onCheckedChange = {
                                        patternVariety = it
                                    }
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TemplateButton(
                            enabled = true,
                            onClick = {
                                showSaveConfigDialog = true
                            },
                            text = stringResource(R.string.save_button_text),
                            icon = Icons.Filled.Save
                        )
                    }
                }

                VerticalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp.scaled()),
                    thickness = 3.dp.scaled(),
                    color = FriendlyLinesColors.PrimaryFriendlyLines.P700
                )

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp.scaled())
                ) {
                    InfoIconButton(
                        infoTitle = stringResource(R.string.info_dialog_title),
                        infoMessage = stringResource(R.string.pattern_preview_info),
                        modifier = Modifier.size(24.dp.scaled()),
                        defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                        activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                        disabledTint = FriendlyLinesColors.Neutral.N300
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(
                                backgroundColor?.color ?: FriendlyLinesColors.Shades.White
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        pattern?.let { patterItem ->
                            PatternPreview(
                                drawing = patterItem.drawing,
                                smoothingEnabled = patterItem.pattern.smoothingEnabled,
                                strokeWidth = strokeWidth,
                                strokeColor = patternColor?.color ?: FriendlyLinesColors.Shades.Black,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp.scaled())
                            )
                        }
                    }
                }
            }
        }
    }

    if (showSaveConfigDialog) {
        TemplateAlertDialog(
            title = stringResource(R.string.save_config_dialog_title),
            message = null,
            confirmText = stringResource(R.string.save_button_text),
            dismissText = stringResource(R.string.dismiss_button_text),
            onConfirm = {
                pattern?.let{ pattern ->
                    showSaveConfigDialog = false

                    if (configId == null) {
                        settingsViewModel.onEvent(
                            LearningStepsSettingsEvent.Patterns(
                                LearningStepsPatternsEvent.AddPattern(
                                    pattern = pattern,
                                    width = selectedWidth,
                                    patternColor = patternColor,
                                    writingColor = writingColor,
                                    backgroundColor = backgroundColor,
                                    patternVariety = patternVariety
                                )
                            )
                        )
                    } else {
                        settingsViewModel.onEvent(
                            LearningStepsSettingsEvent.Patterns(
                                LearningStepsPatternsEvent.UpdatePattern(
                                    configId = configId,
                                    pattern = pattern,
                                    width = selectedWidth,
                                    patternColor = patternColor,
                                    writingColor = writingColor,
                                    backgroundColor = backgroundColor,
                                    patternVariety = patternVariety
                                )
                            )
                        )
                    }
                    onSaveClick(configId)
                }
            },
            onDismiss = {
                showSaveConfigDialog = false
            }
        )
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