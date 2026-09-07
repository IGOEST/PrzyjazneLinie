package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.friendlylines.therapist_app.ui.theme.Neutral300
import com.example.friendlylines.therapist_app.ui.theme.Primary1000
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900

@Composable
fun TemplateAlertDialog(
    title: String,
    message: String?,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
){
    Dialog (
        onDismissRequest = onDismiss
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight()
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(10.dp)
                ),
            shape = RoundedCornerShape(10.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = Primary900,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = Primary900,
                        maxLines = 1,
                        softWrap = false
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    TemplateClickableIcon(
                        icon = Icons.Default.Close,
                        contentDescription = null,
                        onClick = onDismiss,
                        modifier = Modifier.size(24.dp)
                    )
                }

                if (message != null ){
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = message,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        color = Primary1000
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TemplateButton(
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(54.dp),
                        enabled = true,
                        isDialogButton = true,
                        onClick = onDismiss,
                        text = dismissText,
                        icon = null,
                        defaultColor = Color.White,
                        activeColor = Color.White,
                        disabledColor = Color.White,
                        defaultContentColor = Primary700,
                        activeContentColor = Primary900,
                        disabledContentColor = Neutral300
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    TemplateButton(
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(54.dp),
                        enabled = true,
                        isDialogButton = true,
                        onClick = onConfirm,
                        text = confirmText,
                        icon = null,
                        defaultColor = Color.White,
                        activeColor = Color.White,
                        disabledColor = Color.White,
                        defaultContentColor = Primary700,
                        activeContentColor = Primary900,
                        disabledContentColor = Neutral300
                    )
                }
            }
        }
    }
}