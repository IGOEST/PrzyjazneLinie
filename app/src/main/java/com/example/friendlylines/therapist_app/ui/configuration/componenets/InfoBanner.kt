package com.example.friendlylines.therapist_app.ui.configuration.componenets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesModalShape
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled

private val CONTENT_PADDING = 16.dp

@Composable
fun InfoBanner(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = FriendlyLinesColors.PrimaryFriendlyLines.P300,
                    shape = FriendlyLinesModalShape
                )
                .padding(CONTENT_PADDING.scaled()),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = Icons.Rounded.Info,
            contentDescription = null,
            tint = FriendlyLinesColors.PrimaryFriendlyLines.P800,
        )
        Text(
            text = text,
            style = FriendlyLinesTextStyles.captionC1,
            color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
            modifier = Modifier.padding(start = 8.dp.scaled()),
        )
    }
}