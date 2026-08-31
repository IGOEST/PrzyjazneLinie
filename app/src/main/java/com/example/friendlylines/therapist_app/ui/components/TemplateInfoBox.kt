package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composables.core.ScrollArea
import com.composables.core.ScrollAreaState
import com.composables.core.rememberScrollAreaState
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.theme.Neutral400
import com.example.friendlylines.therapist_app.ui.theme.Primary1000
import androidx.compose.foundation.lazy.items

data class InfoRowData(
    val text: String,
    val icon: ImageVector? = Icons.Default.Settings
)

@Composable
fun TemplateInfoBox (
    modifier: Modifier = Modifier,
    info: List<InfoRowData>,
    showSettingsIcon: Boolean,
    scrollable: Boolean = false
) {
    Surface(
        modifier = modifier
            .wrapContentWidth(),
        shape = RoundedCornerShape(10.dp),
        color = Color.White,
        shadowElevation = 24.dp
    ) {
        if (scrollable) {
            val listState = rememberLazyListState()

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(16.dp)
            ) {
                item { InfoHeader() }

                items(info) { row ->
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))

                        InfoRow(
                            text = row.text,
                            icon = row.icon,
                            showIcon = showSettingsIcon
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                InfoHeader()

                info.forEach { row ->
                    Spacer(modifier = Modifier.height(16.dp))

                    InfoRow(
                        text = row.text,
                        icon = row.icon,
                        showIcon = showSettingsIcon
                    )
                }
            }
        }
//        LazyColumn(
//            state = listState,
//            modifier = Modifier.fillMaxSize(),
//            contentPadding = PaddingValues(16.dp)
//        ) {
//            item {
//                Row(
//                    modifier = Modifier.wrapContentWidth(),
//                    horizontalArrangement = Arrangement.Start,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Info,
//                        contentDescription = "Informacje",
//                        tint = Neutral400,
//                        modifier = Modifier.size(24.dp)
//                    )
//
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    Text(
//                        text = stringResource(R.string.info_dialog_title),
//                        color = Neutral400,
//                        fontSize = 16.7.sp,
//                        fontWeight = FontWeight.Normal
//                    )
//                }
//            }
//
//            items(info) { row ->
//                Column {
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                    InfoRow(
//                        text = row.text,
//                        icon = row.icon,
//                        showIcon = showSettingsIcon
//                    )
//                }
//            }
//        }
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
            imageVector = Icons.Default.Info,
            contentDescription = "Informacje",
            tint = Neutral400,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(R.string.info_dialog_title),
            color = Neutral400,
            fontSize = 16.7.sp,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun InfoRow(
    text: String,
    icon: ImageVector? = Icons.Default.Settings,
    showIcon: Boolean = true
) {
    Row(
        modifier = Modifier
            .wrapContentWidth(),
        verticalAlignment = Alignment.Top
    ) {
        if (showIcon && icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Primary1000,
                modifier = Modifier
                    .size(24.dp)
                    .padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
        }

        Text(
            text = text,
            color = Primary1000,
            fontSize = 16.7.sp,
            fontWeight = FontWeight.Normal
        )
    }
}