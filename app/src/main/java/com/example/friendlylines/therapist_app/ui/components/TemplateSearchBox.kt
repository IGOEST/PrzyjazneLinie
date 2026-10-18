package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesShapes
import com.example.friendlylines.theme.FriendlyLinesTextStyles

private val SEARCH_BOX_PADDING = 8.dp
private val SEARCH_ICON_SIZE = 24.dp

@Composable
fun TemplateSearchBox(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .shadow(elevation = 2.dp, shape = FriendlyLinesShapes.extraSmall)
                .background(color = FriendlyLinesColors.Shades.White, shape = FriendlyLinesShapes.extraSmall)
                .padding(SEARCH_BOX_PADDING),
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null,
            tint = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
            modifier = Modifier.size(SEARCH_ICON_SIZE),
        )
        Box(modifier = Modifier.padding(start = 8.dp).fillMaxWidth()) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = FriendlyLinesTextStyles.bodyRegular,
                    color = FriendlyLinesColors.Neutral.N300,
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = FriendlyLinesTextStyles.bodyRegular.copy(
                    color = FriendlyLinesColors.PrimaryFriendlyLines.P1000
                )
            )
        }
    }
}