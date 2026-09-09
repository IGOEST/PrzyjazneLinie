package com.example.friendlylines.child_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composeunstyled.Text
import com.example.friendlylines.R
import com.example.friendlylines.child_app.ui.components.IconType
import com.example.friendlylines.child_app.ui.components.TemplateButton
import com.example.friendlylines.child_app.ui.components.TemplateChildButton
import com.example.friendlylines.child_app.ui.components.TemplateDropdown
import com.example.friendlylines.child_app.ui.theme.Primary1000
import com.example.friendlylines.child_app.ui.theme.Primary50
import com.example.friendlylines.child_app.ui.theme.Primary700
import com.example.friendlylines.child_app.ui.theme.Primary800
import com.example.friendlylines.child_app.ui.theme.White

@Composable
fun ResultsScreen(
    onPlayClick: () -> Unit,
    learningStepResult: LearningStepResult,
    patterns: List<PatternResult>
) {
    var selectedPattern by remember(patterns) { mutableStateOf(patterns.first()) }
    val config = selectedPattern.config

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary50)
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                TemplateButton(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(10.dp),
                    enabled = true,
                    isDialogButton = false,
                    onClick = { },  //TODO: dodaj raport
                    textOne = stringResource(id = R.string.results_download_button_1),
                    textTwo = stringResource(id = R.string.results_download_button_2),
                    icon = Icons.Filled.Checklist,
                    defaultColor = Primary700,
                    activeColor = Primary800,
                    disabledColor = Primary50,
                    defaultContentColor = White,
                    activeContentColor = White,
                    disabledContentColor = Primary50,
                )
                Text(
                    text = stringResource(id = R.string.results),
                    color = Primary1000,
                    fontWeight = FontWeight.Bold,
                    fontSize = 59.7.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
                TemplateChildButton(
                    icon = IconType.Play,
                    onClick = onPlayClick,
                    size = 100.dp,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.results_choose_pattern) + ": ",
                    color = Primary1000,
                    fontWeight = Bold,
                    fontSize = 20.sp
                )
                TemplateDropdown(
                    value = selectedPattern,
                    options = patterns,
                    onOptionSelected = { picked ->
                        selectedPattern = picked ?: patterns.first()
                    },
                    label = "",
                    optionLabel = { it?.patternName ?: "" },
                    width = 300.dp,
                    height = 44.dp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.pattern_color_text) + ": ",
                            color = Primary1000,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = config.patternColor.name,
                            color = Primary1000,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.drawing_color_text) + ": ",
                            color = Primary1000,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = config.traceColor.name,
                            color = Primary1000,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.background_color_text) + ": ",
                            color = Primary1000,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = config.backgroundColor.name,
                            color = Primary1000,
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.pattern_thickness_text) + ": ",
                            color = Primary1000,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = config.patternThickness,
                            color = Primary1000,
                            fontSize = 20.sp
                        )
                    }
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(
                                text = stringResource(id = R.string.results_starting_point_1),
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                            Text(
                                text = stringResource(id = R.string.results_starting_point_2) + ": ",
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(25.dp))
                        if (learningStepResult.showStartingPoint) {
                            Icon(
                                imageVector = Icons.Filled.CheckBox,
                                contentDescription = stringResource(id=R.string.results_yes),
                                tint = Primary700,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        else {
                            Icon(
                                imageVector = Icons.Filled.CheckBoxOutlineBlank,
                                contentDescription = stringResource(id=R.string.results_no),
                                tint = Primary700,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.results_test_mode) + ": ",
                            color = Primary1000,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(25.dp))
                        if (learningStepResult.testMode) {
                            Icon(
                                imageVector = Icons.Filled.CheckBox,
                                contentDescription = stringResource(id=R.string.results_yes),
                                tint = Primary700,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        else {
                            Icon(
                                imageVector = Icons.Filled.CheckBoxOutlineBlank,
                                contentDescription = stringResource(id=R.string.results_no),
                                tint = Primary700,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = stringResource(id = R.string.time_limit) + ": ",
                            color = Primary1000,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = "${learningStepResult.timeLimit} s",
                            color = Primary1000,
                            fontSize = 20.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Primary1000)
                    .padding(20.dp)
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.results_of_learning_step),
                        color = Primary1000,
                        fontWeight = Bold,
                        fontSize = 20.sp
                    )
                    Column {
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = R.string.results_correct_covering) + ": ",
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = "${learningStepResult.correctCoverage}%",
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = R.string.results_line_out_of_bounds) + ": ",
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = "${learningStepResult.lineOutOfBounds}%",
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = R.string.results_correct_shape) + ": ",
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = "${learningStepResult.shapeMatch}%",
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
                Column {
                    Text(
                        text = stringResource(id = R.string.results_of_pattern),
                        color = Primary1000,
                        fontWeight = Bold,
                        fontSize = 20.sp
                    )
                    Column {
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = R.string.results_correct_covering) + ": ",
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = "${selectedPattern.correctCoverage}%",
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = R.string.results_line_out_of_bounds) + ": ",
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = "${selectedPattern.lineOutOfBounds}%",
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(id = R.string.results_correct_shape) + ": ",
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = "${selectedPattern.shapeMatch}%",
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(
                                    text = stringResource(id = R.string.results_correct_starting_point_1),
                                    color = Primary1000,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = stringResource(id = R.string.results_correct_starting_point_2) + ": ",
                                    color = Primary1000,
                                    fontSize = 20.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = if (selectedPattern.correctStartingPoint)
                                    stringResource(id = R.string.results_yes)
                                else
                                    stringResource(id = R.string.results_no),
                                color = Primary1000,
                                fontSize = 20.sp
                            )
                        }
                    }
                }
            }
        }
    }
}