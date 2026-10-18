package com.example.friendlylines.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    message: String? = null,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp.scaled()),
        ) {
            CircularProgressIndicator(
                color = FriendlyLinesColors.PrimaryFriendlyLines.P700,
            )
            if (message != null) {
                Text(
                    text = message,
                    style = FriendlyLinesTextStyles.bodyRegular,
                    color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                )
            }
        }
    }
}