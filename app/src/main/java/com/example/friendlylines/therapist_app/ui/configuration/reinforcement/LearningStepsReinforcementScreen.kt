package com.example.friendlylines.therapist_app.ui.configuration.reinforcement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.ui.components.TemplateButton

private val CONTENT_PADDING = 20.dp

@Composable
fun LearningStepsReinforcementScreen(
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FriendlyLinesColors.PrimaryFriendlyLines.P50)
            .padding(
                CONTENT_PADDING.scaled()
            ),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TemplateButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(54.dp.scaled()),
                enabled = true,
                onClick = onNextClick,
                text = stringResource(R.string.next_button_text),
                icon = Icons.Filled.ArrowForwardIos
            )
        }
    }
}