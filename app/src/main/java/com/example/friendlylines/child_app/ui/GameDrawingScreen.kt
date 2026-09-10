package com.example.friendlylines.child_app.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.friendlylines.R
import com.example.friendlylines.child_app.main.ChildMainEvent
import com.example.friendlylines.child_app.main.ChildViewModel
import com.example.friendlylines.child_app.ui.components.IconType
import com.example.friendlylines.child_app.ui.components.TemplateChildButton
import com.example.friendlylines.child_app.ui.theme.Black
import com.example.friendlylines.child_app.ui.theme.Neutral400
import com.example.friendlylines.child_app.ui.theme.Primary1000
import com.example.friendlylines.child_app.ui.theme.Primary50
import com.example.friendlylines.therapist_app.ui.materials.models.PatternPreview

@Composable
fun GameDrawingScreen(
    onBackClick: () -> Unit,
    viewModel: ChildViewModel = hiltViewModel()
) {
    val gameState by viewModel.state.collectAsState()
    val isMenuOpen = gameState.isMenuOpen
    val currentPatternResult = gameState.patternResults.getOrNull(gameState.currentPattern)
    val backgroundColor = currentPatternResult?.config?.backgroundColor?.color ?: Primary50

    Box(modifier = Modifier.fillMaxSize()) {

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
                    strokeColor = currentPatternResult?.config?.patternColor?.color ?: Primary1000,
                    strokeWidth = currentPatternResult?.config?.patternThicknessDp ?: 10.dp
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
                            color = currentPatternResult?.config?.traceColor?.color ?: Color.Black,
                            style = Stroke(
                                width = (currentPatternResult?.config?.patternThicknessDp ?: 10.dp).toPx(),
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    } else if (stroke.size == 1) {
                        drawCircle(
                            color = currentPatternResult?.config?.traceColor?.color ?: Color.Black,
                            radius = (currentPatternResult?.config?.patternThicknessDp ?: 10.dp).toPx() / 2f,
                            center = stroke.first()
                        )
                    }
                }
            }
        }

        if (isMenuOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black.copy(alpha = 0.4f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { viewModel.onEvent(ChildMainEvent.ToggleMenu) }
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 88.dp, end = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(Neutral400)
                        .padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TemplateChildButton(
                            icon = IconType.StartAgain,
                            onClick = { }, //TODO: restart
                            size = 56.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        TemplateChildButton(
                            icon = IconType.Back,
                            onClick = onBackClick,
                            size = 56.dp
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.child_level) + " " + (gameState.currentPattern + 1) + "/" + gameState.patternResults.size,
                color = Primary1000,
                fontWeight = FontWeight.Bold,
                fontSize = 34.6.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            TemplateChildButton(
                icon = if (isMenuOpen) IconType.CloseMenu else IconType.Menu,
                onClick = { viewModel.onEvent(ChildMainEvent.ToggleMenu) },
                size = 56.dp
            )
        }
    }
}
