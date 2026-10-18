package com.example.friendlylines.therapist_app.ui.configuration.settings.componenets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled

private val TAB_BAR_SHADOW_ELEVATION = 4.dp
private val TAB_ICON_SIZE = 24.dp
private val TAB_UNDERLINE_HEIGHT = 3.dp
private val TAB_BAR_HEIGHT = 78.dp

enum class LearningStepTab {
    PATTERNS,
    LEARNING,
    REINFORCEMENTS,
    TEST,
    SUMMARY,
}
@Composable
fun TemplateTabBar(
    selectedTab: LearningStepTab,
    onTabSelected: (LearningStepTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(TAB_BAR_HEIGHT.scaled())
            .background(FriendlyLinesColors.PrimaryFriendlyLines.P800)
            .shadow(
                elevation = TAB_BAR_SHADOW_ELEVATION.scaled()
            )
    ) {
        LearningStepTab.entries.forEach { tab ->
            val isSelected = tab == selectedTab
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(
                        color = if (isSelected) {
                            FriendlyLinesColors.PrimaryFriendlyLines.P900
                        } else {
                            FriendlyLinesColors.PrimaryFriendlyLines.P800
                        }
                    )
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = {
                            onTabSelected(tab)
                        }
                    )
                    .padding(top = 12.dp.scaled()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val contentColor =
                    if (isSelected) {
                        FriendlyLinesColors.Shades.White
                    } else {
                        FriendlyLinesColors.PrimaryFriendlyLines.P50
                    }

                Icon(
                    imageVector = tab.tabIcon(),
                    contentDescription = stringResource(tab.tabLabel()),
                    tint = contentColor,
                    modifier = Modifier.size(TAB_ICON_SIZE.scaled())
                )

                Spacer(modifier = Modifier.height(8.dp.scaled()))

                Text(
                    text = stringResource(tab.tabLabel()),
                    color = contentColor,
                    style = FriendlyLinesTextStyles.bodyMedium
                )

                Spacer(modifier = Modifier.weight(1f))

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(TAB_UNDERLINE_HEIGHT.scaled())
                            .background(FriendlyLinesColors.Shades.White)
                    )
                }
            }
        }
    }
}

private fun LearningStepTab.tabIcon(): ImageVector =
    when (this) {
        LearningStepTab.PATTERNS -> Icons.Filled.Image
        LearningStepTab.LEARNING -> Icons.Filled.School
        LearningStepTab.REINFORCEMENTS -> Icons.Filled.ThumbUp
        LearningStepTab.TEST -> Icons.Filled.PendingActions
        LearningStepTab.SUMMARY -> Icons.Filled.Checklist
    }

private fun LearningStepTab.tabLabel(): Int =
    when (this) {
        LearningStepTab.PATTERNS -> R.string.patterns_tab_text
        LearningStepTab.LEARNING -> R.string.learning_tab_text
        LearningStepTab.REINFORCEMENTS -> R.string.reinforcements_tab_text
        LearningStepTab.TEST -> R.string.test_tab_text
        LearningStepTab.SUMMARY -> R.string.summary_tab_text
    }

fun LearningStepTab.tabPageInfo(): Int =
    when (this) {
        LearningStepTab.PATTERNS -> R.string.learning_steps_patterns_tab_info_message
        LearningStepTab.LEARNING -> R.string.learning_steps_learning_tab_info_message
        LearningStepTab.REINFORCEMENTS -> R.string.learning_steps_reinforcements_tab_info_message
        LearningStepTab.TEST -> R.string.learning_steps_test_tab_info_message
        LearningStepTab.SUMMARY -> R.string.learning_steps_summary_tab_info_message
    }