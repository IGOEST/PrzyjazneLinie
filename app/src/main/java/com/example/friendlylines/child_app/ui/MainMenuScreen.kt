package com.example.friendlylines.child_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.friendlylines.child_app.ui.theme.Primary50
import com.example.friendlylines.child_app.ui.theme.Primary500
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.friendlylines.R
import com.example.friendlylines.child_app.ui.components.TemplateChildButton
import com.example.friendlylines.child_app.ui.components.IconType
import com.example.friendlylines.child_app.ui.theme.Primary1000


@Composable
fun MainMenuScreen(
    activeStep: String = "Przykład",
    mode: String = "uczenie",
    onPlayClick: () -> Unit)
{
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary50)
    )
    {
        // Bottom bar and mascot
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(100.dp)
                .background(Primary500)
        )

        Image(
            painter = painterResource(id = R.drawable.rybka3),
            contentDescription = "Maskotka",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 40.dp)
                .size(240.dp)
        )
        Column( horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)

        ) {
            // Logo + title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    color = Primary1000,
                    fontWeight = FontWeight.Bold,
                    fontSize = 59.7.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            InfoRow(
                icon = Icons.Filled.AssignmentTurnedIn,
                label = stringResource(R.string.active_learning_step),
                value = activeStep
            )
            Spacer(modifier = Modifier.height(6.dp))
            InfoRow(
                icon = Icons.Filled.Settings,
                label = stringResource(R.string.chosen_mode),
                value = mode
            )
            Spacer(modifier = Modifier.height(30.dp))
            // Play button
            TemplateChildButton(
                icon = IconType.Play,
                onClick = onPlayClick,
                size = 300.dp)
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary1000,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = Primary1000,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = value,
            color = Primary1000,
            fontSize = 24.sp
        )
    }
}
