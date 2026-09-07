package com.example.friendlylines.child_app.main

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.friendlylines.R
import com.example.friendlylines.child_app.theme.LoadingScreenBlue

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun calculateResponsiveFontSize(referenceFontSize: TextUnit): TextUnit {
    val referenceWidth = 2560f
    val referenceHeight = 1600f
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp * configuration.densityDpi / 160f
    val screenHeight = configuration.screenHeightDp * configuration.densityDpi / 160f
    val widthRatio = screenWidth / referenceWidth
    val heightRatio = screenHeight / referenceHeight
    val scalingFactor = kotlin.math.sqrt((widthRatio * heightRatio).toDouble()).toFloat()
    return (referenceFontSize.value * scalingFactor).sp
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun calculateResponsiveDp(
    referenceDp: Dp,
    referenceWidth: Float = 2560f,
    referenceHeight: Float = 1600f
): Dp {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp * configuration.densityDpi / 160f
    val screenHeight = configuration.screenHeightDp * configuration.densityDpi / 160f
    val widthRatio = screenWidth / referenceWidth
    val heightRatio = screenHeight / referenceHeight
    val scalingFactor = kotlin.math.sqrt((widthRatio * heightRatio).toDouble()).toFloat()
    return (referenceDp.value * scalingFactor).dp
}

@Composable
fun InformationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LoadingScreenBlue)
    ) {
        Spacer(modifier = Modifier.height(calculateResponsiveDp(55.dp)))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(calculateResponsiveDp(16.dp)))
            Text(
                text = stringResource(id = R.string.app_name),
                color = Color.White,
                fontSize = calculateResponsiveFontSize(55.sp),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(calculateResponsiveDp(60.dp)))

            Text(
                text = stringResource(id = R.string.loading_screen_1),
                color = Color.White,
                fontSize = calculateResponsiveFontSize(35.sp),
                fontWeight = FontWeight.Bold
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.9f),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 100.dp, topEnd = 100.dp),
        ) {
            Column(
                modifier = Modifier.padding(calculateResponsiveDp(16.dp)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.loading_screen_2),
                    fontSize = calculateResponsiveFontSize(27.sp),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(calculateResponsiveDp(16.dp)))
                Text(
                    text = stringResource(id = R.string.loading_screen_3),
                    fontSize = calculateResponsiveFontSize(26.sp),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(calculateResponsiveDp(16.dp)))
                Text(
                    text = stringResource(id = R.string.loading_screen_4),
                    fontSize = calculateResponsiveFontSize(26.sp),
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = calculateResponsiveFontSize(42.sp)
                )
                Spacer(modifier = Modifier.height(calculateResponsiveDp(16.dp)))

                Text(
                    text = stringResource(id = R.string.loading_screen_5),
                    fontSize = calculateResponsiveFontSize(26.sp),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(calculateResponsiveDp(16.dp)))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(calculateResponsiveDp(85.dp)))
                    Image(
                        painter = painterResource(id = R.drawable.pg_logo),
                        contentDescription = "PG Logo",
                        modifier = Modifier.size(calculateResponsiveDp(350.dp))
                    )
                    Spacer(modifier = Modifier.width(calculateResponsiveDp(95.dp)))
                    Image(
                        painter = painterResource(id = R.drawable.iwrd_logo),
                        contentDescription = "IWRD Logo",
                        modifier = Modifier.size(calculateResponsiveDp(420.dp))
                    )
                    Spacer(modifier = Modifier.width(calculateResponsiveDp(70.dp)))
                    Image(
                        painter = painterResource(id = R.drawable.eti_logo),
                        contentDescription = "ETI Logo",
                        modifier = Modifier.size(calculateResponsiveDp(200.dp))
                    )
                    Spacer(modifier = Modifier.width(calculateResponsiveDp(80.dp)))
                }
            }
        }
    }
}