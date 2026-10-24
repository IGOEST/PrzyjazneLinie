package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesModalShape
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled

private val BOX_SHADOW_ELEVATION = 24.dp
private val BOX_CONTENT_PADDING = 16.dp
private val BOX_ROW_SPACING = 16.dp

data class InfoRowData(
    val text: String,
    val icon: ImageVector? = Icons.Filled.Circle
)

@Composable
fun TemplateInfoBox (
    modifier: Modifier = Modifier,
    info: List<InfoRowData>,
    showBulletIcon: Boolean,
    scrollable: Boolean = false
) {
    Surface(
        modifier = modifier.wrapContentWidth(),
        shape = FriendlyLinesModalShape,
        color = FriendlyLinesColors.Shades.White,
        shadowElevation = BOX_SHADOW_ELEVATION.scaled()
    ) {
        if (scrollable) {
            val listState = rememberLazyListState()

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(BOX_CONTENT_PADDING.scaled()),
                verticalArrangement = Arrangement.spacedBy(BOX_ROW_SPACING.scaled())
            ) {
                item { InfoHeader() }

                items(info) { row ->
                    Column {
                        InfoRow(
                            text = row.text,
                            icon = row.icon,
                            showIcon = showBulletIcon
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(BOX_CONTENT_PADDING.scaled()),
                verticalArrangement = Arrangement.spacedBy(BOX_ROW_SPACING.scaled())
            ) {
                InfoHeader()

                info.forEach { row ->
                    InfoRow(
                        text = row.text,
                        icon = row.icon,
                        showIcon = showBulletIcon
                    )
                }
            }
        }
    }
}

@Composable
fun InfoHeader() {
    Row(
        modifier = Modifier.wrapContentWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = null,
            tint = FriendlyLinesColors.Neutral.N400,
            modifier = Modifier.size(24.dp.scaled())
        )

        Spacer(modifier = Modifier.width(8.dp.scaled()))

        Text(
            text = stringResource(R.string.info_dialog_title),
            color = FriendlyLinesColors.Neutral.N400,
            style = FriendlyLinesTextStyles.captionC1
        )
    }
}

@Composable
fun InfoRow(
    text: String,
    icon: ImageVector? = Icons.Filled.Circle,
    showIcon: Boolean = true
) {
    Row(
        modifier = Modifier.wrapContentWidth(),
        verticalAlignment = Alignment.Top
    ) {
        if (showIcon && icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                modifier = Modifier
                    .size(24.dp.scaled())
                    .padding(top = 2.dp.scaled())
            )
            Spacer(modifier = Modifier.width(10.dp.scaled()))
        }

        Text(
            text = text,
            color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
            style = FriendlyLinesTextStyles.captionC1
        )
    }
}