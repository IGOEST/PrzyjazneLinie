package com.example.friendlylines.therapist_app.backgrounds

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.scaled

private val SHORT_SCREEN_HEIGHT_THRESHOLD = 600.dp
private val SHORT_SCREEN_EXTRA_DOWN_SHIFT = 60.dp
private val SHORT_SCREEN_BUBBLE_EXTRA_DOWN_SHIFT = 40.dp
private const val SHORT_SCREEN_MASCOT_SCALE = 0.75f

private val MASCOT_WIDTH = 200.dp
private val MASCOT_HEIGHT = 180.dp

@Composable
fun MascotWideHelpBackground(
    helpText: String,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {},
) {
    BoxWithConstraints(
        modifier =
            modifier
                .fillMaxSize()
                .background(FriendlyLinesColors.PrimaryFriendlyLines.P50)
                .clipToBounds(),
    ) {
        val isShortScreen = maxHeight < SHORT_SCREEN_HEIGHT_THRESHOLD
        val extraDownShift = if (isShortScreen) SHORT_SCREEN_EXTRA_DOWN_SHIFT.scaled() else 0.dp
        val bubbleExtraDownShift = if (isShortScreen) SHORT_SCREEN_BUBBLE_EXTRA_DOWN_SHIFT.scaled() else 0.dp
        val mascotScale = if (isShortScreen) SHORT_SCREEN_MASCOT_SCALE else 1f
        Image(
            painter = painterResource(R.drawable.background_ellipse),
            contentDescription = null,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 38.dp.scaled(), y = 370.dp.scaled() + extraDownShift)
                    .size(461.dp.scaled()),
        )
        Image(
            painter = painterResource(R.drawable.rybka3),
            contentDescription = null,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-83).dp.scaled(), y = (-58).dp.scaled() + extraDownShift)
                    .size(
                        width = (MASCOT_WIDTH * mascotScale).scaled(),
                        height = (MASCOT_HEIGHT * mascotScale).scaled(),
                    ),
        )
        HelpTextBubble(
            text = helpText,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = 300.dp.scaled(),
                        bottom = 120.dp.scaled() - extraDownShift - bubbleExtraDownShift,
                    )
                    .width(432.dp.scaled()),
        )
        content()
    }
}