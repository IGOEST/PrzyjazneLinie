package com.example.friendlylines.therapist_app.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.friendlylines.therapist_app.ui.theme.IconActiveBG
import com.example.friendlylines.therapist_app.ui.theme.IconDefaultBG
import com.example.friendlylines.therapist_app.ui.theme.Primary300
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900

@Composable
fun TemplateTopAppBar(
    text: String? = null,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    isMainScreen: Boolean = false
) {
    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Primary900.toArgb()
        WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = false
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Primary900)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Primary700,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(
                        horizontal = 16.dp,
                        vertical = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TemplateClickableIcon(
                    icon = Icons.Default.ArrowBack,
                    contentDescription = null,
                    onClick = onBackClick,
                    modifier = Modifier.size(24.dp),
                    defaultTint = IconDefaultBG,
                    activeTint = IconActiveBG,
                    disabledTint = Primary300
                )

                if (text != null) {
                    Spacer(modifier = Modifier.width(32.dp))

                    Text(
                        text = text,
                        fontSize = 24.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                if (!isMainScreen) {
                    TemplateClickableIcon(
                        icon = Icons.Default.Home,
                        contentDescription = null,
                        onClick = onHomeClick,
                        modifier = Modifier.size(24.dp),
                        defaultTint = IconDefaultBG,
                        activeTint = IconActiveBG,
                        disabledTint = Primary300
                    )
                }
            }
        }
    }
}