package com.example.friendlylines.therapist_app.ui.configuration.learning

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateClickableIcon
import com.example.friendlylines.therapist_app.ui.theme.InfoActive
import com.example.friendlylines.therapist_app.ui.theme.InfoDefault
import com.example.friendlylines.therapist_app.ui.theme.Neutral300
import com.example.friendlylines.therapist_app.ui.theme.Primary50

@Composable
fun LearningStepsLearningScreen(
    onNextClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary50)
            .padding(32.dp)
    ) {

        LearningSettingRow(
            title = stringResource(R.string.num_of_repetitions)
        )

        LearningSettingRow(
            title = stringResource(R.string.num_of_attempts)
        )

        LearningSettingRow(
            title = stringResource(R.string.time_limit)
        )

        LearningSettingRow(
            title = stringResource(R.string.level_of_accuracy)
        )

        LearningSettingRow(
            title = stringResource(R.string.starting_point)
        )

        LearningSettingRow(
            title = stringResource(R.string.random_pattern_order)
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TemplateButton(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(54.dp),
                enabled = true,
                onClick = onNextClick,
                text = stringResource(R.string.next_button_text),
                icon = Icons.Outlined.ArrowForwardIos
            )
        }
    }
}

@Composable
fun LearningSettingRow(
    title: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TemplateClickableIcon(
            icon = Icons.Default.Info,
            contentDescription = null,
            onClick = {
                // onInfoClick()
            },
            modifier = Modifier.size(24.dp),
            defaultTint = InfoDefault,
            activeTint = InfoActive,
            disabledTint = Neutral300
        )

        //tu pewnie jeszcze spacer

        Text(
            text = title
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        // tutaj później komponent ustawienia
    }
}