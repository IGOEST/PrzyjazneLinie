package com.example.friendlylines.therapist_app.ui.components

import com.example.friendlylines.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendlylines.therapist_app.ui.theme.Primary1000
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.material3.Icon
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
//import com.example.friendlylines.therapist_app.ui.configuration.config.LearningStepsPatternConfigDraft
import com.example.friendlylines.therapist_app.ui.theme.Primary300
import com.example.friendlylines.therapist_app.ui.theme.Primary500
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900
import com.example.shared.data.drafts.LearningStepsDraft
import com.example.shared.data.drafts.LearningStepsPatternConfigDraft
import com.example.shared.data.entities.LearningStepEntity

@Composable
fun LearningStepsListItem(
    learningStep: LearningStepEntity,
    onEnabledChange: (Boolean) -> Unit,
    onEditClick: () -> Unit,
    onCopyClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onModeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(10.dp)
            )
            .background(
                color = if (learningStep.isActive) Primary500 else Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TemplateCheckbox(
                checked = learningStep.isActive,
                onCheckedChange = onEnabledChange,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = learningStep.name,
                color = Primary1000,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                //modifier = Modifier.weight(1f)
                maxLines = 1
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Uczenie",
                color = Primary1000,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.width(16.dp))

            TemplateToggleSwitch(
                checked = learningStep.isLearning,
                onCheckedChange = {
                    onModeChange(it)
                },
                modifier = Modifier.size(
                    width = 38.dp,
                    height = 24.dp
                )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Test",
                color = Primary1000,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TemplateClickableIcon(
                icon = Icons.Default.Edit,
                contentDescription = "Edytuj krok",
                onClick = onEditClick,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            TemplateClickableIcon(
                icon = Icons.Default.FileCopy,
                contentDescription = "Kopiuj krok",
                onClick = onCopyClick,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            TemplateClickableIcon(
                icon = Icons.Default.Delete,
                contentDescription = "Usuń krok",
                onClick = onDeleteClick,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun LearningStepsListItemAdd(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    Row(
        modifier = modifier
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
            .padding(horizontal = 16.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TemplateClickableIcon(
            icon = Icons.Default.AddCircleOutline,
            contentDescription = "Dodaj własny krok",
            onClick = onClick,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "Add new learning step",
            color = Primary1000,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
        )
    }
}