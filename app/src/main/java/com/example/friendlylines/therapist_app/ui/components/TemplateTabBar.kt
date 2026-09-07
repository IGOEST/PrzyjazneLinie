package com.example.friendlylines.therapist_app.ui.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendlylines.therapist_app.ui.configuration.settings.LearningStepTab
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import com.example.friendlylines.therapist_app.ui.theme.Primary800
import com.example.friendlylines.therapist_app.ui.theme.Primary900

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
            .height(78.dp)
            .background(Primary800)
            .shadow(
                elevation = 4.dp
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
                            Primary900
                        } else {
                            Primary800
                        }
                    )
                    .clickable(
                        indication = null,
                        interactionSource = interactionSource,
                        onClick = {
                            onTabSelected(tab)
                        }
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Icon(
                    imageVector = tab.icon,
                    contentDescription = stringResource(tab.title),
                    tint = if (isSelected) {
                        Color.White
                    } else {
                        Primary50
                    },
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(tab.title),
                    color = if (isSelected) {
                        Color.White
                    } else {
                        Primary50
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.weight(1f))

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(Color.White)
                    )
                }
            }
        }
    }
}