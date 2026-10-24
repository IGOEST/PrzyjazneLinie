package com.example.friendlylines.child_app.backgrounds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.friendlylines.theme.FriendlyLinesColors

@Composable
fun ChildGameEmptyBackground(
    modifier: Modifier = Modifier,
    backgroundColor: Color = FriendlyLinesColors.PrimaryFriendlyLines.P300,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(backgroundColor),
    ) {
        content()
    }
}