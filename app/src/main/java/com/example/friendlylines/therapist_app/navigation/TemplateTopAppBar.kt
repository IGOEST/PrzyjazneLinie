package com.example.friendlylines.therapist_app.navigation

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.ui.components.InfoIconButton
import com.example.friendlylines.therapist_app.ui.components.TemplateClickableIcon

private val NAVBAR_SHADOW_ELEVATION = 8.dp

@Composable
fun TemplateTopAppBar(
    text: String? = null,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    infoTitle: String?,
    infoMessage: String?,
    showHomeIcon: Boolean = true,
    showInfoIcon: Boolean = true
) {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = FriendlyLinesColors.PrimaryFriendlyLines.P900.toArgb()
        WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = false
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(FriendlyLinesColors.PrimaryFriendlyLines.P900)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = FriendlyLinesColors.PrimaryFriendlyLines.P700,
            shadowElevation = NAVBAR_SHADOW_ELEVATION
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp.scaled())
                    .padding(
                        horizontal = 16.dp.scaled(),
                        vertical = 12.5.dp.scaled()
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TemplateClickableIcon(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.topbar_back_description),
                    onClick = onBackClick,
                    modifier = Modifier.size(36.dp.scaled()),
                    defaultTint = FriendlyLinesColors.IconOnBG.IconDefault,
                    activeTint = FriendlyLinesColors.IconOnBG.IconActive,
                    disabledTint = FriendlyLinesColors.IconOnBG.IconDisabled
                )

                if (text != null) {
                    Text(
                        text = text,
                        style = FriendlyLinesTextStyles.headingH5Regular,
                        color = FriendlyLinesColors.Shades.White,
                        modifier = Modifier.padding(start = 16.dp.scaled())
                    )
                }

                if (showInfoIcon) {
                    InfoIconButton(
                        infoTitle = infoTitle,
                        infoMessage = infoMessage,
                        iconSize = 36.dp.scaled(),
                        defaultTint = FriendlyLinesColors.IconOnBG.IconDefault,
                        activeTint = FriendlyLinesColors.IconOnBG.IconActive,
                        disabledTint = FriendlyLinesColors.IconOnBG.IconDisabled,
                        modifier = Modifier.padding(start = 16.dp.scaled())
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                if (showHomeIcon) {
                    TemplateClickableIcon(
                        icon = Icons.Filled.Home,
                        contentDescription = stringResource(R.string.topbar_home_description),
                        onClick = onHomeClick,
                        modifier = Modifier.size(36.dp.scaled()),
                        defaultTint = FriendlyLinesColors.IconOnBG.IconDefault,
                        activeTint = FriendlyLinesColors.IconOnBG.IconActive,
                        disabledTint = FriendlyLinesColors.IconOnBG.IconDisabled
                    )
                }
            }
        }
    }
}