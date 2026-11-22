package com.example.friendlylines.child_app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Icon
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.composeunstyled.Text
import com.example.friendlylines.R
import com.example.friendlylines.child_app.backgrounds.ChildGameEmptyBackground
import com.example.friendlylines.child_app.ui.components.IconType
import com.example.friendlylines.child_app.ui.components.TemplateChildButton
import com.example.friendlylines.name_mapping.ExamplePatternNames
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateDropdown
import com.example.friendlylines.therapist_app.ui.configuration.config.PatternConfigColors
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds
import android.graphics.pdf.PdfDocument
import android.graphics.Paint
import androidx.compose.ui.platform.LocalConfiguration
import com.example.friendlylines.child_app.reports.CsvExporter
import com.example.friendlylines.child_app.reports.PdfExporter
import com.example.friendlylines.child_app.reports.ReportBuilder

@Composable
fun ResultsScreen(
    onPlayClick: () -> Unit,
    learningStepResult: LearningStepResult,
    patterns: List<PatternResult>
) {
    var selectedPattern by remember(patterns) { mutableStateOf(patterns.first()) }
    val config = selectedPattern.config
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var saveMessage by remember { mutableStateOf<Int?>(null) }

    val timestamp = SimpleDateFormat("yyyy.MM.dd_HH.mm", Locale.ROOT).format(Date())

    val fileName = "${stringResource(R.string.report_name)}_$timestamp.pdf"

    LaunchedEffect(saveMessage) {
        if(saveMessage != null) {
            delay(2000.milliseconds)
            saveMessage = null
        }
    }

    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf")
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val saved = withContext(Dispatchers.IO) {
                    runCatching {
                        val report = ReportBuilder.buildPdfReport(learningStepResult, patterns)
                        PdfExporter.save(context, uri, report)
                    }.isSuccess
                }

                saveMessage = if (saved) {
                    R.string.saved_report_message
                } else {
                    R.string.not_saved_report_message
                }
            }
        }
    }

    ChildGameEmptyBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp.scaled()),
            verticalArrangement = Arrangement.spacedBy(8.dp.scaled()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            saveMessage?.let { messageId ->
                Text(
                    text = stringResource(messageId),
                    color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                    style = FriendlyLinesTextStyles.headingH4Medium
                )
            }

            Row(
                modifier = Modifier
                    .padding(16.dp.scaled())
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TemplateButton(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(16.dp.scaled()),
                        enabled = true,
                        onClick = { pdfLauncher.launch(fileName) },
                        text = stringResource(id = R.string.results_download_button_1) + "\n" + stringResource(
                            id = R.string.results_download_button_2
                        ),
                        icon = Icons.Filled.Checklist
                    )
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(id = R.string.results),
                        color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                        style = FriendlyLinesTextStyles.displayD2
                    )
                    TemplateChildButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        icon = IconType.Play,
                        onClick = onPlayClick,
                        size = 100.dp.scaled()
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(24.dp.scaled()),
                horizontalArrangement = Arrangement.spacedBy(16.dp.scaled()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val patternResultsScroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(patternResultsScroll),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp.scaled())
                ) {
                    Text(
                        text = stringResource(id = R.string.results_of_pattern),
                        color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                        style = FriendlyLinesTextStyles.headingH4Medium
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp.scaled())
                    ) {
                        Text(
                            text = stringResource(id = R.string.results_choose_pattern) + ": ",
                            color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                            style = FriendlyLinesTextStyles.bodyMedium
                        )
                        TemplateDropdown(
                            value = selectedPattern,
                            options = patterns,
                            onOptionSelected = { picked ->
                                selectedPattern = picked ?: patterns.first()
                            },
                            label = "",
                            dropdownWidth = 350.dp.scaled(),
                            optionLabel = { pattern ->
                                pattern?.let {
                                    val name = it.exampleKey?.let { key ->
                                        ExamplePatternNames.examplePatternNameFor(key)
                                    } ?: it.originalName

                                    "$name ${it.displayNumber}"
                                } ?: ""
                            }
                        )
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp.scaled())
                    ) {
                        ResultInfoRow(
                            label = stringResource(id = R.string.pattern_color_text) + ": ",
                            value = PatternConfigColors.nameFor(config.patternColor.key)
                        )
                        ResultInfoRow(
                            label = stringResource(id = R.string.drawing_color_text) + ": ",
                            value = PatternConfigColors.nameFor(config.traceColor.key)
                        )
                        ResultInfoRow(
                            label = stringResource(id = R.string.background_color_text) + ": ",
                            value = PatternConfigColors.nameFor(config.backgroundColor.key)
                        )
                        ResultInfoRow(
                            label = stringResource(id = R.string.pattern_thickness_text) + ": ",
                            value = when (config.patternThickness) {
                                "THIN" -> stringResource(R.string.thin_label_text)
                                "MEDIUM" -> stringResource(R.string.medium_label_text)
                                "THICK" -> stringResource(R.string.thick_label_text)
                                else -> config.patternThickness
                            }
                        )
                        ResultInfoRow(
                            label = stringResource(id = R.string.results_correct_covering) + ": ",
                            value = "${selectedPattern.insideArea}%"
                        )
                        ResultInfoRow(
                            label = stringResource(id = R.string.results_line_out_of_bounds) + ": ",
                            value = "${selectedPattern.outsideArea}%"
                        )
                        ResultInfoRow(
                            label = stringResource(id = R.string.results_correct_shape) + ": ",
                            value = "${selectedPattern.shapeMatch}%"
                        )
                        ResultInfoRow(
                            label = stringResource(id = R.string.results_correct_starting_point_1) + " " + stringResource(
                                id = R.string.results_correct_starting_point_2
                            ) + ": ",
                            value = if (learningStepResult.showStartingPoint) {
                                if (selectedPattern.correctStartingPoint)
                                    stringResource(id = R.string.results_yes)
                                else
                                    stringResource(id = R.string.results_no)
                            } else {
                                stringResource(id = R.string.not_applicable)
                            }
                        )
                    }
                }
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 16.dp.scaled()),
                    thickness = 3.dp.scaled(),
                    color = FriendlyLinesColors.PrimaryFriendlyLines.P700
                )
                val stepResultsScroll = rememberScrollState()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(stepResultsScroll),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(32.dp.scaled())
                ) {
                    Text(
                        text = stringResource(id = R.string.results_of_learning_step),
                        color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                        style = FriendlyLinesTextStyles.headingH4Medium
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(16.dp.scaled())
                    ) {
                        ResultInfoRow(
                            label = stringResource(id = R.string.results_correct_covering) + ": ",
                            value = "${learningStepResult.correctCoverage}%"
                        )
                        ResultInfoRow(
                            label = stringResource(id = R.string.results_line_out_of_bounds) + ": ",
                            value = "${learningStepResult.lineOutOfBounds}%"
                        )
                        ResultInfoRow(
                            label = stringResource(id = R.string.results_correct_shape) + ": ",
                            value = "${learningStepResult.shapeMatch}%"
                        )
                        ResultInfoRow(
                            label = stringResource(id = R.string.results_starting_point_1) + " " + stringResource(
                                id = R.string.results_starting_point_2
                            ) + ": ",
                            icon = if (learningStepResult.showStartingPoint) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank
                        )
                        ResultInfoRow(
                            label = stringResource(id = R.string.results_test_mode) + ": ",
                            icon = if (learningStepResult.testMode) Icons.Filled.CheckBox else Icons.Filled.CheckBoxOutlineBlank
                        )
                        ResultInfoRow(
                            label = stringResource(id = R.string.time_limit) + ": ",
                            value = "${learningStepResult.timeLimit} s"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultInfoRow(
    modifier: Modifier = Modifier,
    label: String,
    value: String? = null,
    icon: ImageVector? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
            style = FriendlyLinesTextStyles.headingH4Regular
        )
        if (icon == null && value != null) {
            Text(
                text = value,
                color = FriendlyLinesColors.PrimaryFriendlyLines.P1000,
                style = FriendlyLinesTextStyles.headingH4Regular
            )
        } else if (icon != null && value == null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = FriendlyLinesColors.PrimaryFriendlyLines.P700,
                modifier = Modifier.size(32.dp.scaled())
            )
        }
    }
}