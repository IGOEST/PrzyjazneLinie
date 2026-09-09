package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendlylines.therapist_app.ui.theme.Primary900
import com.example.shared.data.entities.LearningStepEntity
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import com.example.friendlylines.therapist_app.ui.theme.Primary1000
import com.example.friendlylines.therapist_app.ui.theme.Primary500
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.friendlylines.R
import com.example.shared.data.entities.LearningStepMode
import androidx.compose.material.icons.outlined.AddCircle


@Composable
fun LearningStepListArea(
    learningSteps: List<LearningStepEntity>,
    onDeleteClick: (LearningStepEntity) -> Unit = {},
    onActiveStepClick: (LearningStepEntity) -> Unit = {},
    onModeChange: (LearningStepEntity, Boolean) -> Unit = { _, _ -> },
    onCreateClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(
                horizontal = 24.dp,
                vertical = 8.dp
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(
                items = learningSteps,
                key = { it.id }
            ) { learningStep ->

                LearningStepListItem(
                    learningStep = learningStep,
                    onDeleteClick = {
                        onDeleteClick(learningStep)
                    },
                    onActiveStepClick = {
                        onActiveStepClick(learningStep)
                    },
                    onModeChange = {
                            isTest ->
                        onModeChange(learningStep, isTest)
                    },
                    onEditClick = {
                        // later
                    },
                    onCopyClick = {
                        // later
                    }
                )
            }
            item {
                CreateLearningStepListItem(
                    onClick = onCreateClick
                )
            }
        }
    }

}

@Composable
private fun LearningStepListItem(
    learningStep: LearningStepEntity,
    onDeleteClick: () -> Unit,
    onActiveStepClick: () -> Unit,
    onModeChange: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onCopyClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = if (learningStep.activeStep) {
                    Primary500
                } else {
                    Color.White
                },
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TemplateCheckbox(
            checked = learningStep.activeStep,
            onCheckedChange = {
                onActiveStepClick()
            },
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = learningStep.name,
            color = Primary1000,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = stringResource(R.string.learning_mode),
            color = Primary900,
            fontSize = 16.7.sp
        )

        Spacer(modifier = Modifier.width(8.dp))

        TemplateToggleSwitch(
            checked = learningStep.mode == LearningStepMode.TEST.name,
            onCheckedChange = onModeChange,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = stringResource(R.string.test_mode),
            color = Primary900,
            fontSize = 16.7.sp
        )

        Spacer(modifier = Modifier.width(30.dp))

        TemplateClickableIcon(
            icon = Icons.Default.Edit,
            contentDescription = stringResource(R.string.edit_learning_step),
            onClick = onEditClick,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        TemplateClickableIcon(
            icon = Icons.Default.FileCopy,
            contentDescription = stringResource(R.string.copy_learning_step),
            onClick = onCopyClick,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        TemplateClickableIcon(
            icon = Icons.Default.Delete,
            contentDescription = stringResource(R.string.delete_learning_step),
            onClick = onDeleteClick,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun CreateLearningStepListItem(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TemplateClickableIcon(
            icon = Icons.Outlined.AddCircle,
            contentDescription = stringResource(R.string.create_learning_step),
            onClick = onClick,
            modifier = Modifier.size(36.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = stringResource(R.string.create_learning_step),
            color = Primary1000,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal
        )
    }
}