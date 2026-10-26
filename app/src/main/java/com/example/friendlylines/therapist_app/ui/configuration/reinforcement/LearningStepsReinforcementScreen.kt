package com.example.friendlylines.therapist_app.ui.configuration.reinforcement

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.backgrounds.MascotNarrowHelpBackground
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateCheckbox
import com.example.shared.data.drafts.LearningStepsReinforcementsDraft
import kotlin.math.round

private val CONTENT_PADDING = 20.dp
private val COLUMN_GAP = 30.dp
private val ROW_GAP = 7.dp
private val SECTION_SPACING = 20.dp
private const val CONTENT_WIDTH_FRACTION = 0.72f
private const val CHECKBOX_SECTION_WIDTH_FRACTION = 0.5f

@Composable
fun LearningStepsReinforcementScreen(
    onNextClick: () -> Unit,
    draft: LearningStepsReinforcementsDraft,
    onEvent: (LearningStepsReinforcementsEvent) -> Unit,
    viewModel: LearningStepsReinforcementsScreenViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FriendlyLinesColors.PrimaryFriendlyLines.P50)
    ) {
        MascotNarrowHelpBackground(
            helpText = stringResource(R.string.reinforcements_help_text),
            modifier = Modifier.weight(1f),
        ) {
            Column(modifier = Modifier.fillMaxWidth(CONTENT_WIDTH_FRACTION).fillMaxHeight().padding(CONTENT_PADDING.scaled())) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(40.dp.scaled()),
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(SECTION_SPACING.scaled()),
                    ) {
                        Text(
                            text = stringResource(R.string.reinforcements_praise_header) + ":",
                            style = FriendlyLinesTextStyles.headingH5Regular,
                            color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                        )
                        OptionCheckboxGroup(
                            options = LearningStepsReinforcementsDraft.PRAISE_WORDS,
                            checked = draft.enabledPraiseWords,
                            allowEmpty = false,
                            label = { option ->
                                stringResource(option.praiseWordLabelRes())
                            },
                            onCheckedChanged = { words ->
                                onEvent(
                                    LearningStepsReinforcementsEvent.SetEnabledPraiseWords(
                                        words = words, words.isNotEmpty()
                                    )
                                )
                            }
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(SECTION_SPACING.scaled()),
                    ) {
                        Text(
                            text = stringResource(R.string.reinforcements_animation_header) + ":",
                            style = FriendlyLinesTextStyles.headingH5Regular,
                            color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                        )
                        OptionCheckboxGroup(
                            options = LearningStepsReinforcementsDraft.ANIMATION_THEMES,
                            checked = draft.enabledAnimationThemes,
                            allowEmpty = true,
                            label = { option ->
                                stringResource(option.animationThemeLabelRes())
                            },
                            onCheckedChanged = { themes ->
                                onEvent(
                                    LearningStepsReinforcementsEvent.SetEnabledAnimationThemes(
                                        themes = themes, themes.isNotEmpty()
                                    )
                                )
                            }
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(SECTION_SPACING.scaled()),
                    ) {
                        Text(
                            text = stringResource(R.string.reinforcements_end_header) + ":",
                            style = FriendlyLinesTextStyles.headingH5Regular,
                            color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp.scaled())) {
                            CheckboxRow(
                                checked = draft.endSessionAnimationEnabled,
                                label = stringResource(R.string.reinforcements_end_animation_label),
                                onCheckedChange = { enabled ->
                                    onEvent(LearningStepsReinforcementsEvent.SetEndSessionAnimationEnabled(enabled))
                                },
                            )
                            CheckboxRow(
                                checked = draft.endSessionFanfareEnabled,
                                label = stringResource(R.string.reinforcements_end_fanfare_label),
                                onCheckedChange = { enabled ->
                                    onEvent(LearningStepsReinforcementsEvent.SetEndSessionFanfareEnabled(enabled))
                                },
                            )
                        }
                    }
                }
                TemplateButton(
                    enabled = true,
                    onClick = onNextClick,
                    text = stringResource(R.string.next_button_text),
                    icon = Icons.Filled.ArrowForwardIos,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
private fun OptionCheckboxGroup(
    options: Set<String>,
    checked: Set<String>,
    allowEmpty: Boolean,
    label: @Composable (String) -> String,
    onCheckedChanged: (Set<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val optionList = options.toList()
    val leftColumnSize = (optionList.size + 1) / 2
    val columns = listOf(optionList.take(leftColumnSize), optionList.drop(leftColumnSize))

    CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
        Row(
            modifier = modifier.fillMaxWidth(CHECKBOX_SECTION_WIDTH_FRACTION),
            horizontalArrangement = Arrangement.spacedBy(COLUMN_GAP.scaled()),
        ) {
            columns.forEach { column ->
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(ROW_GAP.scaled())) {
                    column.forEach { option ->
                        CheckboxRow(
                            checked = option in checked,
                            label = label(option),
                            onCheckedChange = { newChecked ->
                                val next = if (newChecked) checked + option else checked - option
                                if (allowEmpty || next.isNotEmpty()) onCheckedChanged(next)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CheckboxRow(
    checked: Boolean,
    label: String,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp.scaled()),
        modifier = Modifier.fillMaxWidth()
    ) {
        TemplateCheckbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.size(18.dp.scaled())
        )
        Text(
            text = label,
            style = FriendlyLinesTextStyles.bodyRegular,
            color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
        )
    }
}

internal fun String.animationThemeLabelRes(): Int =
    when (this) {
        "flowers" -> R.string.reinforcements_theme_flowers
        "butterflies" -> R.string.reinforcements_theme_butterflies
        "balloons" -> R.string.reinforcements_theme_balloons
        "cars" -> R.string.reinforcements_theme_cars
        "balls" -> R.string.reinforcements_theme_balls
        else -> R.string.reinforcements_theme_flowers
    }

internal fun String.praiseWordLabelRes(): Int =
    when (this) {
        "dobrze" -> R.string.reinforcements_praise_good
        "super" -> R.string.reinforcements_praise_super
        "świetnie" -> R.string.reinforcements_praise_great
        "brawo" -> R.string.reinforcements_praise_bravo
        else -> R.string.reinforcements_praise_bravo
    }