package com.example.friendlylines.therapist_app.ui.configuration.summary.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.composables.core.ScrollArea
import com.composables.core.Thumb
import com.composables.core.VerticalScrollbar
import com.composables.core.rememberScrollAreaState
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.ui.configuration.summary.LearningStepsSummaryState
import com.example.friendlylines.therapist_app.ui.materials.components.SCROLLBAR_SHAPE
import com.example.friendlylines.therapist_app.ui.materials.components.SCROLLBAR_WIDTH

private val BOX_BORDER_WIDTH = 3.dp
private val BOX_BORDER_SHAPE = 2.dp
private val BOX_ROW_HEIGHT = 54.dp

@Composable
fun LearningStepSummaryBox(
    state: LearningStepsSummaryState,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scrollAreaState = rememberScrollAreaState(listState)

    Box(
        modifier = modifier
    ) {
        ScrollArea(
            state = scrollAreaState,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .border(
                            width = BOX_BORDER_WIDTH.scaled(),
                            color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                            shape = RoundedCornerShape(BOX_BORDER_SHAPE.scaled())
                        )
                        .padding(4.dp.scaled())
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            SummaryHeaderRow(
                                label = stringResource(R.string.step_info),
                                learningValue = stringResource(R.string.learning_mode_summary),
                                testValue = stringResource(R.string.test_mode_summary)
                            )
                        }

                        items(state.rows, key = {it.label}) { row ->
                            SummaryRow(
                                label = row.label,
                                learningValue = row.learningValue,
                                testValue = row.testValue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp.scaled()))

                VerticalScrollbar(
                    modifier = Modifier
                        .width(SCROLLBAR_WIDTH)
                        .fillMaxHeight()
                ) {
                    Thumb(
                        modifier = Modifier
                            .background(
                                color = FriendlyLinesColors.PrimaryFriendlyLines.P700,
                                shape = RoundedCornerShape(SCROLLBAR_SHAPE)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryHeaderRow(
    label: String,
    learningValue: String,
    testValue: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(BOX_ROW_HEIGHT.scaled())
                .padding(8.dp.scaled()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = FriendlyLinesTextStyles.headingH5Regular,
                color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                modifier = Modifier.weight(2f)
            )
            Text(
                text = learningValue,
                style = FriendlyLinesTextStyles.headingH5Regular,
                color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = testValue,
                style = FriendlyLinesTextStyles.headingH5Regular,
                color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    learningValue: String,
    testValue: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(BOX_ROW_HEIGHT.scaled())
                .padding(8.dp.scaled()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = FriendlyLinesTextStyles.captionC1,
                color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                modifier = Modifier.weight(2f)
            )
            Text(
                text = learningValue,
                style = FriendlyLinesTextStyles.captionC1,
                color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = testValue,
                style = FriendlyLinesTextStyles.captionC1,
                color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
        HorizontalDivider(
            color = FriendlyLinesColors.PrimaryFriendlyLines.P700,
            modifier = Modifier.padding(horizontal = 4.dp.scaled())
        )
    }
}