package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900
import com.example.shared.data.entities.LearningStepEntity

@Composable
fun LearningStepListArea(
    learningSteps: List<LearningStepEntity>,
    onLearningStepClick: (LearningStepEntity) -> Unit = {},
    onDeleteClick: (LearningStepEntity) -> Unit = {},
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
            .border(
                width = 2.dp,
                color = Primary900,
                shape = RoundedCornerShape(2.dp)
            )
            .padding(4.dp)
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
                    onClick = {
                        onLearningStepClick(learningStep)
                    },
                    onDeleteClick = {
                        onDeleteClick(learningStep)
                    }
                )
            }
        }
    }

}

@Composable
private fun LearningStepListItem(
    learningStep: LearningStepEntity,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Primary50)
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = learningStep.name,
                modifier = Modifier.weight(1f),
                color = Primary900,
                fontSize = 20.sp
            )

            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Usuń krok",
                    tint = Primary700
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Otwórz krok",
                tint = Primary700
            )

            Spacer(
                modifier = Modifier.width(8.dp)
            )
        }

        HorizontalDivider(
            color = Primary700,
            thickness = 1.dp
        )
    }
}
