package com.example.friendlylines.child_app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.child_app.ui.components.IconType
import com.example.friendlylines.child_app.ui.components.TemplateChildButton
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import pg.autyzm.friendlyemotions.child.backgrounds.ChildGameStartBackground

private val LOGO_SIZE = 140.dp
private val PLAY_BUTTON_SIZE = 300.dp

@Composable
fun MainMenuScreen(
    activeStep: String = "",
    mode: String = "",
    canPlay: Boolean,
    onPlayClick: () -> Unit,
) {
    ChildGameStartBackground {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 48.dp.scaled(), vertical = 32.dp.scaled()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp.scaled())
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = null,
                    modifier = Modifier.size(LOGO_SIZE.scaled())
                )
                Text(
                    text = stringResource(R.string.app_name),
                    color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                    style = FriendlyLinesTextStyles.displayD1
                )
            }
            Spacer(modifier = Modifier.height(16.dp.scaled()))
            Column(
                modifier = Modifier.padding(8.dp.scaled()),
                verticalArrangement = Arrangement.spacedBy(8.dp.scaled()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                InfoRow(
                    icon = Icons.Filled.AssignmentTurnedIn,
                    label = stringResource(R.string.active_learning_step),
                    value = activeStep
                )
                InfoRow(
                    icon = Icons.Filled.Settings,
                    label = stringResource(R.string.chosen_mode),
                    value = mode
                )
            }
            Spacer(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .heightIn(min = 50.dp.scaled()),
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp.scaled()),
            ) {
                TemplateChildButton(
                    icon = IconType.Play,
                    onClick = onPlayClick,
                    size = PLAY_BUTTON_SIZE.scaled(),
                    enabled = canPlay
                )
                if (!canPlay) {
                    Text(
                        text = stringResource(R.string.child_home_cannot_play_message),
                        style = FriendlyLinesTextStyles.bodyRegular,
                        color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp.scaled())
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
            modifier = Modifier.size(32.dp.scaled())
        )
        Text(
            text = label,
            color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
            style = FriendlyLinesTextStyles.headingH3Medium
        )
        Text(
            text = value,
            color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
            style = FriendlyLinesTextStyles.headingH3Regular
        )
    }
}
