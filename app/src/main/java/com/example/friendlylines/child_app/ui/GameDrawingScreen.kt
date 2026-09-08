package com.example.friendlylines.child_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendlylines.R
import com.example.friendlylines.child_app.components.ChildButton
import com.example.friendlylines.child_app.components.IconType
import com.example.friendlylines.child_app.theme.Primary1000
import com.example.friendlylines.child_app.theme.Primary50
import com.example.friendlylines.child_app.theme.Black
import com.example.friendlylines.child_app.theme.Neutral400

@Composable
fun GameDrawingScreen(
    onBackClick: () -> Unit)
{
    var isMenuOpen by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary50) //TODO:zmień na background levelu
        ) {
            //TODO:tutaj rysowanie
        }

        if (isMenuOpen) {
            //TODO: zatrzymaj stoper
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black.copy(alpha = 0.4f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { /* blocks drawing when menu open */ }
            )

            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 88.dp, end = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(Neutral400)
                        .padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ChildButton(
                            icon = IconType.StartAgain,
                            onClick = { }, //TODO: restart
                            size = 56.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        ChildButton(
                            icon = IconType.Back,
                            onClick = onBackClick,
                            size = 56.dp
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                //TODO:zmień na poziom/liczba poziomów
                text = stringResource(id = R.string.child_level) + " " + "1" + "/" + "1",
                color = Primary1000,
                fontWeight = FontWeight.Bold,
                fontSize = 34.6.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            ChildButton(
                icon = if (isMenuOpen) IconType.CloseMenu else IconType.Menu,
                onClick = { isMenuOpen = !isMenuOpen }, //TODO: kontynuuj / zatrzymaj stoper
                size = 56.dp
            )
        }
    }
}