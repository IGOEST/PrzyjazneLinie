package com.example.friendlylines.therapist_app.backgrounds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesModalShape
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled

@Composable
internal fun BoxScope.HelpTextBubble(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = FriendlyLinesColors.PrimaryFriendlyLines.P300,
) {
    Box(
        modifier =
            modifier
                .background(color = containerColor, shape = FriendlyLinesModalShape)
                .padding(10.dp.scaled()),
    ) {
        Text(
            text = text,
            style = FriendlyLinesTextStyles.bodyRegular,
            color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}