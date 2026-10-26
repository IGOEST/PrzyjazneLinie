package com.example.friendlylines.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.theme.*

/**
 * Screen with information about application, that is shown
 */
@Composable
fun InfoSplashScreen(
    appTitle: String,
    @DrawableRes appIconRes: Int,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Primary50)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onContinue,
                ),
    ) {
        Image(
            painter = painterResource(R.drawable.background_ellipse),
            contentDescription = null,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .offset(
                        x = 90.dp.scaled(),
                        y = 370.dp.scaled(),
                    )
                    .size(550.dp.scaled()),
        )
        Image(
            painter = painterResource(R.drawable.rybka3),
            contentDescription = null,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 70.dp.scaled(), bottom = 140.dp.scaled())
                    .size(200.dp.scaled()),
        )
        SpeechBubble(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 70.dp.scaled(), bottom = 350.dp.scaled())
                    .widthIn(max = 200.dp.scaled()),
        )

        Column(verticalArrangement = Arrangement.spacedBy(20.dp.scaled())) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp.scaled(), vertical = 40.dp.scaled()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp.scaled()),
                ) {
                    Image(
                        painter = painterResource(appIconRes),
                        contentDescription = null,
                        modifier = Modifier.size(90.dp.scaled()),
                    )
                    Text(
                        text = appTitle,
                        style = FriendlyLinesTextStyles.displayD2,
                        color = Primary1000,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp.scaled()))
                Text(
                    text = stringResource(R.string.core_ui_splash_subtitle),
                    style = FriendlyLinesTextStyles.headingH5Regular,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
            }

            Column(
                modifier =
                    Modifier
                        .padding(start = 70.dp.scaled())
                        .widthIn(max = 800.dp.scaled())
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .padding(16.dp.scaled())
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp.scaled()),
            ) {
                SPLASH_BULLETS.forEach { bullet ->
                    BulletRow(bullet.emojiRes, bullet.labelRes, bullet.bodyRes)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(130.dp.scaled())
                        .background(Color.White.copy(alpha = 0.5f)),
                horizontalArrangement = Arrangement.spacedBy(64.dp.scaled(), Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    painter = painterResource(R.drawable.pg_eti_logo),
                    contentDescription = null,
                    modifier = Modifier.height(80.dp.scaled()),
                    contentScale = ContentScale.FillHeight,
                )
                Image(
                    painter = painterResource(R.drawable.iwrd_logo),
                    contentDescription = null,
                    modifier = Modifier.height(80.dp.scaled()),
                    contentScale = ContentScale.FillHeight,
                )
            }
        }
    }
}

@Composable
private fun SpeechBubble(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .background(Primary300, RoundedCornerShape(10.dp.scaled()))
                .padding(12.dp.scaled()),
    ) {
        Text(
            text = stringResource(R.string.core_ui_splash_continue_hint),
            style = FriendlyLinesTextStyles.bodyRegular,
            color = Primary1000,
            textAlign = TextAlign.Center,
        )
    }
}

private data class SplashBullet(
    @DrawableRes val emojiRes: Int,
    val labelRes: Int,
    val bodyRes: Int,
)

private val SPLASH_BULLETS =
    listOf(
        SplashBullet(R.drawable.no_money, R.string.core_ui_splash_bullet1_label, R.string.core_ui_splash_bullet1_body),
        SplashBullet(R.drawable.school, R.string.core_ui_splash_bullet2_label, R.string.core_ui_splash_bullet2_body),
        SplashBullet(R.drawable.puzzle, R.string.core_ui_splash_bullet3_label, R.string.core_ui_splash_bullet3_body),
        SplashBullet(
            R.drawable.teddy_bear,
            R.string.core_ui_splash_bullet4_label,
            R.string.core_ui_splash_bullet4_body,
        ),
        SplashBullet(R.drawable.world, R.string.core_ui_splash_bullet5_label, R.string.core_ui_splash_bullet5_body),
    )

@Composable
private fun BulletRow(
    @DrawableRes emojiRes: Int,
    labelRes: Int,
    bodyRes: Int,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp.scaled()),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(emojiRes),
            contentDescription = null,
            modifier = Modifier.size(36.dp.scaled()),
        )
        Text(
            text = bulletText(labelRes, bodyRes),
            style = FriendlyLinesTextStyles.bodyRegular,
            color = Color.Black,
        )
    }
}

@Composable
private fun bulletText(
    labelRes: Int,
    bodyRes: Int,
): AnnotatedString {
    val label = stringResource(labelRes)
    val body = stringResource(bodyRes)
    val linkStart = body.indexOf("https://")
    return buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(label)
        }
        append(" ")
        if (linkStart >= 0) {
            append(body.substring(0, linkStart))
            val url = body.substring(linkStart)
            withLink(
                LinkAnnotation.Url(
                    url = url,
                    styles = TextLinkStyles(style = SpanStyle(textDecoration = TextDecoration.Underline)),
                ),
            ) {
                append(url)
            }
        } else {
            append(body)
        }
    }
}
