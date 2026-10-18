package com.example.friendlylines.therapist_app.ui.materials.create_new

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesModalShape
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.backgrounds.PlainBackground
import com.example.friendlylines.therapist_app.navigation.ExitDestination
import com.example.friendlylines.therapist_app.ui.components.InfoRowData
import com.example.friendlylines.therapist_app.ui.components.TemplateAlertDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateInfoBox
import com.example.friendlylines.therapist_app.ui.components.TemplateSaveDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateToggleSwitch
import com.example.friendlylines.therapist_app.navigation.TemplateTopAppBar
import com.example.friendlylines.therapist_app.ui.materials.geometry.clipSegmentToRectangle
import com.example.friendlylines.therapist_app.ui.materials.geometry.isInside
import com.example.friendlylines.therapist_app.ui.materials.geometry.toRectangle
import com.example.friendlylines.therapist_app.ui.materials.create_new.componenets.DrawingCanvas
import com.example.friendlylines.therapist_app.ui.materials.create_new.componenets.DrawingStroke
import com.example.friendlylines.therapist_app.ui.materials.create_new.componenets.toNormalizedStroke
import java.lang.System.currentTimeMillis

private val CONTENT_PADDING = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePatternScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSaveSuccess: (Long) -> Unit,
) {
    val rightListState = rememberLazyListState()

    val viewModel: CreatePatternScreenViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val strokes = remember {
        mutableStateListOf<DrawingStroke>()
    }

    val currentStroke = remember {
        mutableStateListOf<Offset>()
    }

    var straightLineStart by remember {
        mutableStateOf<Offset?>(null)
    }

    val isComplexPattern = strokes.size >= 2

    var canvasSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    var drawingBlocked by remember {
        mutableStateOf(false)
    }

    BackHandler {
        viewModel.onEvent(
            CreatePatternScreenEvent.ExitRequested(
                ExitDestination.PREVIOUS
            )
        )
    }

    LaunchedEffect(state.savedPatternId) {
        val patternId = state.savedPatternId ?: return@LaunchedEffect

        strokes.clear()
        currentStroke.clear()

        onSaveSuccess(patternId)

        viewModel.onEvent(
            CreatePatternScreenEvent.SaveSuccessHandled
        )
    }

    LaunchedEffect(state.showExitDialog, state.exitDestination) {
        if (state.showExitDialog) {
            return@LaunchedEffect
        }

        val destination = state.exitDestination ?: return@LaunchedEffect

        viewModel.onEvent(
            CreatePatternScreenEvent.ExitNavigationHandled
        )

        when (destination) {
            ExitDestination.PREVIOUS -> onBackClick()
            ExitDestination.HOME -> onHomeClick()
        }
    }

    Scaffold(
        containerColor = FriendlyLinesColors.PrimaryFriendlyLines.P50,
        topBar = {
            TemplateTopAppBar(
                text = null,
                onBackClick = {
                    viewModel.onEvent(
                        CreatePatternScreenEvent.ExitRequested(
                            ExitDestination.PREVIOUS
                        )
                    )
                },
                onHomeClick = {
                    viewModel.onEvent(
                        CreatePatternScreenEvent.ExitRequested(
                            ExitDestination.HOME
                        )
                    )
                },
                infoTitle = null,
                infoMessage = null,
                showInfoIcon = false
            )
        }
    ) { padding ->
        PlainBackground(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(CONTENT_PADDING.scaled()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp.scaled())
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    TemplateInfoBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        info = listOf(
                            InfoRowData(
                                text = stringResource(R.string.draw_start_info)
                            ),
                            InfoRowData(
                                text = stringResource(R.string.simple_pattern_info)
                            ),
                            InfoRowData(
                                text = stringResource(R.string.complex_pattern_info)
                            ),
                            InfoRowData(
                                text = stringResource(R.string.smooth_info)
                            ),
                            InfoRowData(
                                text = stringResource(R.string.straight_line_info)
                            )
                        ),
                        showBulletIcon = true,
                        scrollable = true
                    )
                }

//            Spacer(modifier = Modifier.width(24.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxHeight()
                        .onSizeChanged {
                            canvasSize = it
                        }
                        .background(
                            color = FriendlyLinesColors.Shades.White,
                            shape = FriendlyLinesModalShape
                        )
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    if (!offset.isInside(canvasSize))
                                        return@detectDragGestures
                                    drawingBlocked = false
                                    currentStroke.clear()

                                    if (state.straightLineEnabled) {
                                        straightLineStart = offset
                                        currentStroke.add(offset)
                                    } else {
                                        currentStroke.add(offset)
                                    }
                                },
                                onDrag = { change, _ ->
                                    if (drawingBlocked)
                                        return@detectDragGestures

                                    val position = change.position
                                    if (position.isInside(canvasSize)) {
                                        if (state.straightLineEnabled) {
                                            currentStroke.clear()
                                            currentStroke.add(position)
                                        } else {
                                            currentStroke.add(position)
                                        }
                                    } else {
                                        if (currentStroke.isEmpty()) {
                                            return@detectDragGestures
                                        }

                                        val clipped = clipSegmentToRectangle(
                                            start = currentStroke.last(),
                                            end = position,
                                            rectangle = canvasSize.toRectangle()
                                        )
                                        if (state.straightLineEnabled) {
                                            currentStroke.clear()
                                            currentStroke.add(clipped)
                                        } else {
                                            currentStroke.add(clipped)
                                        }

                                        strokes.add(
                                            DrawingStroke(
                                                points = if (state.straightLineEnabled) {
                                                    listOf(
                                                        straightLineStart ?: currentStroke.first(),
                                                        clipped
                                                    )
                                                } else {
                                                    currentStroke.toList()
                                                }
                                            )
                                        )
                                        currentStroke.clear()
                                        straightLineStart = null
                                        drawingBlocked = true
                                    }
                                },
                                onDragEnd = {
                                    drawingBlocked = false
                                    if (state.straightLineEnabled) {
                                        val start = straightLineStart
                                        val end = currentStroke.lastOrNull()
                                        if (start != null && end != null) {
                                            strokes.add(
                                                DrawingStroke(
                                                    points = listOf(
                                                        start,
                                                        end
                                                    )
                                                )
                                            )
                                        }
                                        currentStroke.clear()
                                        straightLineStart = null
                                    } else {
                                        if (currentStroke.isNotEmpty()) {
                                            strokes.add(
                                                DrawingStroke(
                                                    points = currentStroke.toList()
                                                )
                                            )
                                        }
                                        currentStroke.clear()
                                    }
                                },
                                onDragCancel = {
                                    currentStroke.clear()
                                    drawingBlocked = false
                                    straightLineStart = null
                                }
                            )
                        }
                ) {
                    DrawingCanvas(
                        strokes = strokes,
                        currentStroke = currentStroke,
                        smoothingEnabled = state.smoothingEnabled,
                        straightLineEnabled = state.straightLineEnabled,
                        straightLineStart = straightLineStart
                    )
                }

//            Spacer(modifier = Modifier.width(24.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn(
                        state = rightListState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 80.dp.scaled()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp.scaled()),
                        contentPadding = PaddingValues(
                            horizontal = CONTENT_PADDING.scaled()
                        )
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TemplateToggleSwitch(
                                    checked = state.smoothingEnabled,
                                    onCheckedChange = {
                                        viewModel.onEvent(
                                            CreatePatternScreenEvent.SmoothingEnabledChanged(it)
                                        )
                                    },
//                                modifier = Modifier.size(
//                                    width = 38.dp,
//                                    height = 24.dp
//                                )
                                )

                                Spacer(modifier = Modifier.width(16.dp.scaled()))

                                Text(
                                    text = stringResource(R.string.smooth_drawing_text),
                                    style = FriendlyLinesTextStyles.bodyRegular,
                                    color = FriendlyLinesColors.PrimaryFriendlyLines.P1000
                                )
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TemplateToggleSwitch(
                                    checked = state.straightLineEnabled,
                                    onCheckedChange = {
                                        viewModel.onEvent(
                                            CreatePatternScreenEvent.StraightLineEnabledChanged(it)
                                        )
                                    },
//                                modifier = Modifier.size(
//                                    width = 38.dp,
//                                    height = 24.dp
//                                )
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Text(
                                    text = stringResource(R.string.straight_line_text),
                                    style = FriendlyLinesTextStyles.bodyRegular,
                                    color = FriendlyLinesColors.PrimaryFriendlyLines.P1000
                                )
                            }
                        }

                        item {
                            TemplateButton(
                                modifier = Modifier.fillMaxWidth(0.9f),
                                enabled = strokes.isNotEmpty(),
                                onClick = {
                                    strokes.clear()
                                    currentStroke.clear()
                                },
                                text = stringResource(R.string.clear_drawing_button_text),
                                icon = Icons.Filled.Close
                            )
                        }

                        if (isComplexPattern) {
                            item {
                                TemplateInfoBox(
                                    modifier = Modifier.fillMaxWidth(0.9f),
                                    info = listOf(
                                        InfoRowData(text = stringResource(R.string.complex_pattern_alert_message))
                                    ),
                                    showBulletIcon = false,
                                    scrollable = false
                                )
                            }
                        }
                    }

                    TemplateButton(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .align(Alignment.BottomCenter),
                        enabled = strokes.isNotEmpty(),
                        onClick = {
                            viewModel.onEvent(
                                CreatePatternScreenEvent.SaveClicked
                            )
                        },
                        text = stringResource(R.string.save_button_text),
                        icon = Icons.Filled.Save
                    )
                }
            }
        }
    }

    if (state.showSaveDialog) {
        TemplateSaveDialog(
            title = stringResource(R.string.save_pattern_dialog_title),
            confirmText = stringResource(R.string.save_button_text),
            dismissText = stringResource(R.string.dismiss_button_text),
            textFieldLabel = stringResource(R.string.save_name_field_title),
            name = state.patternName,
            onNameChange = {
                viewModel.onEvent(
                    CreatePatternScreenEvent.PatternNameChanged(it)
                )
            },
            nameError = state.patternNameError,
            onDismiss = {
                viewModel.onEvent(
                    CreatePatternScreenEvent.SaveDismissed
                )
            },
            onSave = {
                val normalizedStrokes = strokes.map { stroke ->
                    stroke.toNormalizedStroke(canvasSize)
                }

                viewModel.onEvent(
                    CreatePatternScreenEvent.SaveConfirmed(
                        strokes = normalizedStrokes,
                        isComplex = strokes.size >= 2,
                        createdAt = currentTimeMillis()
                    )
                )
            }
        )
    }

    if (state.showExitDialog) {
        TemplateAlertDialog(
            title = stringResource(R.string.exit_dialog_title),
            message = stringResource(R.string.exit_dialog_message),
            confirmText = stringResource(R.string.exit_confirm_button_text),
            dismissText = stringResource(R.string.dismiss_button_text),
            onConfirm = {
                viewModel.onEvent(
                    CreatePatternScreenEvent.ExitConfirmed
                )
            },
            onDismiss = {
                viewModel.onEvent(
                    CreatePatternScreenEvent.ExitDismissed
                )
            }
        )
    }
}