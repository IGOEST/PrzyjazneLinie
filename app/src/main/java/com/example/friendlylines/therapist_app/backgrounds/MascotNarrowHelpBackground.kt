package com.example.friendlylines.therapist_app.backgrounds

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.scaled

@Composable
fun MascotNarrowHelpBackground(
    helpText: String,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(FriendlyLinesColors.PrimaryFriendlyLines.P50)
                .clipToBounds(),
    ) {
        Image(
            painter = painterResource(R.drawable.background_ellipse),
            contentDescription = null,
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 269.dp.scaled(), y = (-266).dp.scaled())
                    .size(461.dp.scaled()),
        )
        Image(
            painter = painterResource(R.drawable.purple_ellipse),
            contentDescription = null,
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-60).dp.scaled(), y = 13.dp.scaled())
                    .size(78.dp.scaled()),
        )
        Image(
            painter = painterResource(R.drawable.green_leaf),
            contentDescription = null,
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-51).dp.scaled(), y = 32.dp.scaled())
                    .size(width = 86.dp.scaled(), height = 143.dp.scaled())
                    .rotate(16f),
        )
        Image(
            painter = painterResource(R.drawable.background_ellipse),
            contentDescription = null,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 38.dp.scaled(), y = 353.dp.scaled())
                    .size(461.dp.scaled()),
        )
        Image(
            painter = painterResource(R.drawable.rybka3),
            contentDescription = null,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-76).dp.scaled(), y = (-70).dp.scaled())
                    .size(width = 229.dp.scaled(), height = 199.dp.scaled()),
        )
        HelpTextBubble(
            text = helpText,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 95.dp.scaled(), bottom = 286.dp.scaled())
                    .width(206.dp.scaled()),
        )
        content()
    }
}
