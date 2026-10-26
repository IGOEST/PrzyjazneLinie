package com.example.friendlylines.child_app.ui

import android.media.MediaPlayer
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.friendlylines.R
import com.example.friendlylines.child_app.backgrounds.ChildGameEmptyBackground
import com.example.friendlylines.child_app.main.ChildMainEvent
import com.example.friendlylines.child_app.main.ChildViewModel
import com.example.friendlylines.child_app.reinforcements.FloatingSpriteOverlay
import com.example.friendlylines.child_app.reinforcements.ReinforcementEvent
import com.example.friendlylines.child_app.ui.components.IconType
import com.example.friendlylines.child_app.ui.components.TemplateChildButton
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.ui.materials.create_new.componenets.PatternPreview

@Composable
fun GameDrawingScreen(
    onBackClick: () -> Unit,
    viewModel: ChildViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    var activeAnimationTheme by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(viewModel, context) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ReinforcementEvent.PlayPraise -> {
                    val resourceId = praiseResourceIds(effect.word).randomOrNull()

                    if (resourceId == null) {
                        viewModel.onPraisePlaybackFinished()
                        return@collect
                    }

                    val player = MediaPlayer.create(context, resourceId)

                    if (player == null) {
                        viewModel.onPraisePlaybackFinished()
                        return@collect
                    }

                    var handled = false


                    fun finishPlayback() {
                        if (!handled) {
                            handled = true
                            player.release()
                            viewModel.onPraisePlaybackFinished()
                        }
                    }

                    player.setOnCompletionListener {
                        finishPlayback()
                    }

                    player.setOnErrorListener { _, _, _ ->
                        finishPlayback()
                        true
                    }

                    try {
                        player.start()
                    } catch (e: IllegalStateException) {
                        finishPlayback()
                    }
                }

                is ReinforcementEvent.PlayAnimation -> {
                    activeAnimationTheme = effect.theme
                }
            }
        }
    }

    val gameState by viewModel.state.collectAsState()
    val isMenuOpen = gameState.isMenuOpen
    val currentPatternResult = gameState.patternResults.getOrNull(gameState.currentPattern)
    val backgroundColor = currentPatternResult?.config?.backgroundColor?.color ?: FriendlyLinesColors.Shades.White

    ChildGameEmptyBackground(
        backgroundColor = backgroundColor
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .pointerInput(isMenuOpen) {
                        if (isMenuOpen) return@pointerInput
                        detectDragGestures(
                            onDragStart = { offset ->
                                viewModel.onEvent(ChildMainEvent.OnPointerDown(offset))
                            },
                            onDrag = { change, _ ->
                                viewModel.onEvent(ChildMainEvent.OnPointerMove(change.position))
                            },
                            onDragEnd = {
                                viewModel.onEvent(ChildMainEvent.OnPointerUp)
                            },
                            onDragCancel = {
                                viewModel.onEvent(ChildMainEvent.OnPointerUp)
                            }
                        )
                    }
            ) {
                gameState.currentPatternDrawing?.let { drawing ->
                    PatternPreview(
                        drawing = drawing,
                        smoothingEnabled = currentPatternResult?.config?.smoothingEnabled ?: true,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .fillMaxHeight(0.7f)
                            .align(Alignment.Center),
                        strokeColor = currentPatternResult?.config?.patternColor?.color ?: FriendlyLinesColors.Shades.Black,
                        strokeWidth = currentPatternResult?.config?.patternThicknessDp ?: 10.dp.scaled(),
                        showStartingPoint = (gameState.learningStepResult?.testMode == false && gameState.learningStepResult?.showStartingPoint == true),
                        patternVariety = currentPatternResult?.config?.patternVariety ?: false,
                        varietyKey = gameState.currentPattern
                    )
                }

                Canvas(modifier = Modifier.fillMaxSize()) {
                    gameState.userStrokes.forEach { stroke ->
                        if (stroke.size > 1) {
                            val path = Path()
                            path.moveTo(stroke.first().x, stroke.first().y)
                            for (i in 1 until stroke.size) {
                                path.lineTo(stroke[i].x, stroke[i].y)
                            }
                            drawPath(
                                path = path,
                                color = currentPatternResult?.config?.traceColor?.color ?: FriendlyLinesColors.Shades.Black,
                                style = Stroke(
                                    width = (currentPatternResult?.config?.patternThicknessDp ?: 10.dp).toPx(),
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        } else if (stroke.size == 1) {
                            drawCircle(
                                color = currentPatternResult?.config?.traceColor?.color ?: FriendlyLinesColors.Shades.Black,
                                radius = (currentPatternResult?.config?.patternThicknessDp ?: 10.dp).toPx() / 2f,
                                center = stroke.first()
                            )
                        }
                    }
                }

                activeAnimationTheme?.let { theme ->
                    FloatingSpriteOverlay(
                        animationTheme = theme,
                        modifier = Modifier.fillMaxSize(),
                        onFinished = {
                            activeAnimationTheme = null
                            viewModel.onAnimationFinished()
                        }
                    )
                }
            }

            if (isMenuOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FriendlyLinesColors.Shades.Black.copy(alpha = 0.4f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            viewModel.onEvent(ChildMainEvent.ToggleMenu)
                        }
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .statusBarsPadding()
                        .padding(
                            top = 88.dp.scaled(),
                            end = 16.dp.scaled()
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .background(FriendlyLinesColors.Neutral.N400)
                            .padding(8.dp.scaled())
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp.scaled())
                        ) {
                            TemplateChildButton(
                                icon = IconType.StartAgain,
                                onClick = { } //TODO: restart
                            )
                            TemplateChildButton(
                                icon = IconType.Back,
                                onClick = onBackClick
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp.scaled()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.child_level) + " " + (gameState.currentPattern + 1) + "/" + gameState.patternResults.size,
                    color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                    style = FriendlyLinesTextStyles.headingH3Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                TemplateChildButton(
                    icon = if (isMenuOpen) IconType.CloseMenu else IconType.Menu,
                    onClick = { viewModel.onEvent(ChildMainEvent.ToggleMenu) },
                    modifier = Modifier.padding(8.dp.scaled())
                )
            }
        }
    }
}

private fun praiseResourceIds(word: String): List<Int> =
    when (word) {
        "dobrze" -> listOf(
            R.raw.dobrze1,
            R.raw.dobrze2,
            R.raw.dobrze3,
            R.raw.dobrze4
        )

        "super" -> listOf(
            R.raw.super1,
            R.raw.super2,
            R.raw.super3,
            R.raw.super4
        )

        "świetnie" -> listOf(
            R.raw.swietnie1,
            R.raw.swietnie2,
            R.raw.swietnie3,
            R.raw.swietnie4
        )

        "brawo" -> listOf(
            R.raw.brawo1,
            R.raw.brawo2,
            R.raw.brawo3
        )

        else -> emptyList()
    }
