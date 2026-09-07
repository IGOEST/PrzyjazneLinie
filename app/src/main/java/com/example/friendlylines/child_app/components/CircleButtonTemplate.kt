package com.example.friendlylines.child_app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.friendlylines.child_app.theme.Success
import com.example.friendlylines.child_app.theme.White
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.friendlylines.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.ui.draw.shadow

enum class IconType(val contentDescription: Int) {
    Play(R.string.child_play),
    Menu(R.string.child_options),
    CloseMenu(R.string.child_close_options),
    Back(R.string.child_back),
    StartAgain(R.string.child_start_again)
}

private fun IconType.toImageVector() = when (this) {
    IconType.Menu -> Icons.Filled.Menu
    IconType.Play -> Icons.Filled.PlayArrow
    IconType.CloseMenu -> Icons.AutoMirrored.Filled.MenuOpen
    IconType.Back -> Icons.AutoMirrored.Filled.ArrowBack
    IconType.StartAgain -> Icons.Filled.Refresh
}


@Composable
fun ChildButton(
    icon: IconType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    iconSizeFraction: Float = 0.9f,
    contentDescription: Int = icon.contentDescription,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(White)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon.toImageVector(),
            contentDescription = stringResource(contentDescription),
            tint = Success,
            modifier = Modifier.size(size * iconSizeFraction)
        )
    }
}
