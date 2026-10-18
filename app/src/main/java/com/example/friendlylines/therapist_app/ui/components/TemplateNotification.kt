package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesModalShape
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled

@Composable
fun TemplateNotification(
    snackbarData: SnackbarData
) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .background(
                color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                shape = FriendlyLinesModalShape
            )
            .padding(
                horizontal = 24.dp.scaled(),
                vertical = 12.dp.scaled()
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = snackbarData.visuals.message,
            color = FriendlyLinesColors.Shades.White,
            style = FriendlyLinesTextStyles.bodyRegular,
            textAlign = TextAlign.Center
        )
    }
}