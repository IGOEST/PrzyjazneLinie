package com.example.friendlylines.therapist_app.ui.configuration.config

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.friendlylines.therapist_app.ui.components.TemplateAlertDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateClickableIcon
import com.example.friendlylines.therapist_app.ui.components.TemplateDropdown
import com.example.friendlylines.therapist_app.ui.components.TemplateInfoDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateSlider
import com.example.friendlylines.therapist_app.ui.components.TemplateToggleSwitch
import com.example.friendlylines.therapist_app.ui.components.TemplateTopAppBar
import com.example.friendlylines.therapist_app.ui.configuration.config.PatternConfigOptions.toDp
import com.example.friendlylines.therapist_app.ui.configuration.patterns.LearningStepsPatternsScreenViewModel
import com.example.friendlylines.therapist_app.ui.main.ExitDestination
import com.example.friendlylines.therapist_app.ui.main.NavRoutes
import com.example.friendlylines.therapist_app.ui.materials.models.PatternPreview
import com.example.friendlylines.therapist_app.ui.theme.*

@Composable
fun LearningStepPatternConfigScreen(
    navController: NavController,
    patternId: Long,
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

    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(
            NavRoutes.LEARNING_STEPS_CREATE
        )
    }
    val learningStepsPatternsViewModel: LearningStepsPatternsScreenViewModel = hiltViewModel(parentEntry)
    val patterns by learningStepsPatternsViewModel.patternsDraft.collectAsStateWithLifecycle()

    val existingConfig = remember(patterns, configId) {
        patterns.firstOrNull {
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

    val availablePatternColors = PatternConfigOptions.patternAndWriting.filter { option ->
        option != writingColor
    }

    val availableWritingColors = PatternConfigOptions.patternAndWriting.filter { option ->
        option != patternColor
    }

    val availableBackgroundColors = PatternConfigOptions.background

    var showThicknessInfoDialog by remember {
        mutableStateOf(false)
    }

    var showVarietyInfoDialog by remember {
        mutableStateOf(false)
    }

    var showPatternPreviewInfoDialog by remember {
        mutableStateOf(false)
    }

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
    val leftScrollAreaState = rememberScrollAreaState(leftListState)

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
        containerColor = Primary50,
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
                }
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
                    .padding(
                        horizontal = 32.dp,
                        vertical = 16.dp
                    )
            ) {
                ScrollArea(
                    state = leftScrollAreaState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(end = 32.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            state = leftListState,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            contentPadding = PaddingValues(
                                bottom = 16.dp
                            )
                        ) {
                            item {
                                TemplateSlider(
                                    values = thicknessValues,
                                    selectedIndex = selectedWidth.ordinal,
                                    onValueSelected = { index ->
                                        selectedWidth = PatternWidth.entries[index]
                                    },
                                    showHeader = true,
                                    headerText = stringResource(R.string.pattern_thickness_text) + ":",
                                    headerIcon = Icons.Default.Info,
                                    onHeaderIconClick = {
                                        showThicknessInfoDialog = true
                                    }
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
                                    optionLabel = { it?.name ?: "" },
                                    showEmptyOption = true,
                                    showHeader = true,
                                    headerText = stringResource(R.string.pattern_color_text),
                                    width = 350.dp,
                                    height = 54.dp,
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
                                    optionLabel = { it?.name ?: "" },
                                    showEmptyOption = true,
                                    showHeader = true,
                                    headerText = stringResource(R.string.drawing_color_text),
                                    width = 350.dp,
                                    height = 54.dp,
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
                                    optionLabel = { it?.name ?: "" },
                                    showEmptyOption = true,
                                    showHeader = true,
                                    headerText = stringResource(R.string.background_color_text),
                                    width = 350.dp,
                                    height = 54.dp,
                                )
                            }

                            item {
                                Row(
                                    modifier = Modifier
                                        .width(350.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TemplateClickableIcon(
                                        icon = Icons.Default.Info,
                                        contentDescription = null,
                                        onClick = {
                                            showVarietyInfoDialog = true
                                        },
                                        defaultTint = InfoDefault,
                                        activeTint = InfoActive,
                                        modifier = Modifier.size(24.dp)
                                    )

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Text(
                                        text = stringResource(R.string.pattern_variety_text),
                                        color = Primary1000,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Normal
                                    )

                                    Spacer(modifier = Modifier.weight(1f))

                                    TemplateToggleSwitch(
                                        checked = patternVariety,
                                        onCheckedChange = {
                                            patternVariety = it
                                        },
                                        modifier = Modifier.size(
                                            width = 38.dp,
                                            height = 24.dp
                                        )
                                    )
                                }
                            }

                            item {
                                TemplateButton(
                                    modifier = Modifier
                                        .height(54.dp)
                                        .wrapContentWidth(),
                                    enabled = true,
                                    onClick = {
                                        showSaveConfigDialog = true
                                    },
                                    text = stringResource(R.string.save_button_text),
                                    icon = Icons.Default.Save
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(48.dp))

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

                VerticalDivider(
                    thickness = 1.dp,
                    color = Primary700
                )

                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                        .fillMaxHeight(),
                    verticalAlignment = Alignment.Top
                ) {
                    TemplateClickableIcon(
                        icon = Icons.Default.Info,
                        contentDescription = null,
                        onClick = {
                            showPatternPreviewInfoDialog = true
                        },
                        defaultTint = InfoDefault,
                        activeTint = InfoActive,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(
                                backgroundColor?.color ?: Color.White
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        pattern?.let { patterItem ->
                            PatternPreview(
                                drawing = patterItem.drawing,
                                smoothingEnabled = patterItem.pattern.smoothingEnabled,
                                strokeWidth = strokeWidth,
                                strokeColor = patternColor?.color ?: Color.Black,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (showThicknessInfoDialog) {
        TemplateInfoDialog(
            title = stringResource(R.string.pattern_thickness_text),
            info = stringResource(R.string.pattern_thickness_info),
            onDismiss = {
                showThicknessInfoDialog = false
            }
        )
    }

    if (showVarietyInfoDialog) {
        TemplateInfoDialog(
            title = stringResource(R.string.pattern_variety_text),
            info = stringResource(R.string.pattern_variety_info),
            onDismiss = {
                showVarietyInfoDialog = false
            }
        )
    }

    if (showPatternPreviewInfoDialog) {
        TemplateInfoDialog(
            title = stringResource(R.string.info_dialog_title),
            info = stringResource(R.string.pattern_preview_info),
            onDismiss = {
                showPatternPreviewInfoDialog = false
            }
        )
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
                        val newConfigId = learningStepsPatternsViewModel.addPattern(
                            pattern = pattern,
                            width = selectedWidth,
                            patternColor = patternColor,
                            writingColor = writingColor,
                            backgroundColor = backgroundColor,
                            patternVariety = patternVariety
                        )
                        onSaveClick(newConfigId)
                    } else {
                        learningStepsPatternsViewModel.updatePattern(
                            configId = configId,
                            patternItem = pattern,
                            width = selectedWidth,
                            patternColor = patternColor,
                            writingColor = writingColor,
                            backgroundColor = backgroundColor,
                            patternVariety = patternVariety
                        )
                        onSaveClick(configId)
                    }
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