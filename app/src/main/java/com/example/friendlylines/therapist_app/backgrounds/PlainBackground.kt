package com.example.friendlylines.therapist_app.backgrounds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.friendlylines.theme.FriendlyLinesColors

@Composable
fun PlainBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(FriendlyLinesColors.PrimaryFriendlyLines.P50),
        content = content,
    )
}