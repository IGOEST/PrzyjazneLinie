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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileCopy
import androidx.compose.ui.res.stringResource
import com.example.friendlylines.therapist_app.ui.configuration.config.LearningStepsPatternConfigDraft
import com.example.friendlylines.therapist_app.ui.theme.Primary300

@Composable
fun LearningStepsPatternItem(
    index: Int,
    pattern: LearningStepsPatternConfigDraft,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    onMoveUpClick: () -> Unit,
    onMoveDownClick: () -> Unit,
    onEditClick: () -> Unit,
    onCopyClick: () -> Unit,
    onDeleteClick: () -> Unit,
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
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TemplateCheckbox(
            checked = pattern.isEnabled,
            onCheckedChange = onEnabledChange,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = stringResource(R.string.pattern_text) + " ${pattern.order + 1} (${pattern.pattern.pattern.name})",
            color = Primary1000,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )

        TemplateClickableIcon(
            icon = Icons.Default.ArrowDropUp,
            contentDescription = "Przenieś wyżej",
            onClick = onMoveUpClick,
            enabled = canMoveUp,
            modifier = Modifier.size(24.dp),
            disabledTint = Primary300
        )

        Spacer(modifier = Modifier.width(16.dp))

        TemplateClickableIcon(
            icon = Icons.Default.ArrowDropDown,
            contentDescription = "Przenieś niżej",
            onClick = onMoveDownClick,
            enabled = canMoveDown,
            modifier = Modifier.size(24.dp),
            disabledTint = Primary300
        )

        Spacer(modifier = Modifier.width(64.dp))

        TemplateClickableIcon(
            icon = Icons.Default.Edit,
            contentDescription = "Edytuj wzór",
            onClick = onEditClick,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        TemplateClickableIcon(
            icon = Icons.Default.FileCopy,
            contentDescription = "Kopiuj wzór",
            onClick = onCopyClick,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        TemplateClickableIcon(
            icon = Icons.Default.Delete,
            contentDescription = "Usuń wzór",
            onClick = onDeleteClick,
            modifier = Modifier.size(24.dp)
        )
    }
}