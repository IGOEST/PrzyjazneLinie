package com.example.friendlylines.child_app.reinforcements

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

private const val SPRITE_COUNT = 8
private const val BASE_DURATION_MILLIS = 3_500
private const val DURATION_JITTER_MILLIS = 500
private const val MIN_SCALE = 0.4f
private const val MAX_SCALE = 0.6f
private const val MAX_START_DELAY_MILLIS = 800L
private val spriteBaseSize = 384.dp

private val themeDrawables: Map<String, List<Int>> = mapOf(
    "flowers" to listOf(
        R.drawable.animation_flower_orange,
        R.drawable.animation_flower_pink,
        R.drawable.animation_flower_purple
    ),
    "butterflies" to listOf(
        R.drawable.animation_butterfly_blue,
        R.drawable.animation_butterfly_green,
        R.drawable.animation_butterfly_purple
    ),
    "balloons" to listOf(
        R.drawable.animation_balloon_blue,
        R.drawable.animation_balloon_green,
        R.drawable.animation_balloon_red,
        R.drawable.animation_balloon_yellow
    ),
    "cars" to listOf(
        R.drawable.animation_car_green,
        R.drawable.animation_car_red,
        R.drawable.animation_car_yellow
    ),
    "balls" to listOf(
        R.drawable.animation_ball_1,
        R.drawable.animation_ball_2,
        R.drawable.animation_ball_3
    )
)

private data class SpriteSpec(
    val drawableRes: Int,
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float,
    val scale: Float,
    val durationMillis: Int,
    val startDelayMillis: Long
)

@Composable
fun FloatingSpriteOverlay(
    animationTheme: String,
    modifier: Modifier = Modifier,
    onFinished: () -> Unit
) {
    val assets = themeDrawables[animationTheme].orEmpty()

    LaunchedEffect(animationTheme, assets) {
        if (assets.isEmpty()) onFinished()
    }

    if (assets.isEmpty()) return

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
    ) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }
        val spriteSizePx = with(density) { spriteBaseSize.toPx() }

        val sprites = remember(
            animationTheme,
            widthPx,
            heightPx,
            spriteSizePx,
            assets
        ) {
            List(SPRITE_COUNT) { index ->
                createSpriteSpec(
                    index = index,
                    assets = assets,
                    travelHorizontally = animationTheme == "cars",
                    widthPx = widthPx,
                    heightPx = heightPx,
                    spriteSizePx = spriteSizePx
                )
            }
        }

        val totalDuration = sprites.maxOf {
            it.startDelayMillis + it.durationMillis
        }

        LaunchedEffect(animationTheme, sprites) {
            delay(totalDuration)
            onFinished()
        }

        Box(Modifier.fillMaxSize()) {
            sprites.forEachIndexed { index, spec ->
                key("$animationTheme-$index") {
                    keySprite(
                        key = "$animationTheme-$index",
                        spec = spec
                    )
                }
            }
        }
    }
}

@Composable
private fun keySprite(
    key: String,
    spec: SpriteSpec
) {
    val progress = remember(key) { Animatable(0f) }

    LaunchedEffect(key, spec) {
        delay(spec.startDelayMillis)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = spec.durationMillis,
                easing = LinearEasing
            )
        )
    }

    val x = spec.startX + (spec.endX - spec.startX) * progress.value
    val y = spec.startY + (spec.endY - spec.startY) * progress.value

    Image(
        painter = painterResource(spec.drawableRes),
        contentDescription = null,
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .size(spriteBaseSize * spec.scale)
            .graphicsLayer {
                translationX = x
                translationY = y
            }
    )
}

private fun createSpriteSpec(
    index: Int,
    assets: List<Int>,
    travelHorizontally: Boolean,
    widthPx: Float,
    heightPx: Float,
    spriteSizePx: Float
): SpriteSpec {
    val random = Random(index * 31 + assets.hashCode())

    val scale = MIN_SCALE + random.nextFloat() * (MAX_SCALE - MIN_SCALE)

    val sized = spriteSizePx * scale

    val duration = (
            BASE_DURATION_MILLIS + random.nextInt(
                        -DURATION_JITTER_MILLIS,
                        DURATION_JITTER_MILLIS + 1
                    )
            ).coerceAtLeast(1_000)

    val startDelay = random.nextLong(0L, MAX_START_DELAY_MILLIS + 1)

    val drawable = assets[index % assets.size]

    return if (travelHorizontally) {
        val startY = random.nextFloat() * (heightPx - sized).coerceAtLeast(0f)

        SpriteSpec(
            drawableRes = drawable,
            startX = -sized,
            startY = startY,
            endX = widthPx + sized,
            endY = (startY + (random.nextFloat() - 0.5f) * 80f)
                .coerceIn(0f, (heightPx - sized)
                    .coerceAtLeast(0f)),
            scale = scale,
            durationMillis = duration,
            startDelayMillis = startDelay
        )
    } else {
        val startX = random.nextFloat() * (widthPx - sized).coerceAtLeast(0f)

        SpriteSpec(
            drawableRes = drawable,
            startX = startX,
            startY = heightPx,
            endX = (startX + (random.nextFloat() - 0.5f) * 60f)
                .coerceIn(0f, (widthPx - sized)
                    .coerceAtLeast(0f)),
            endY = -sized,
            scale = scale,
            durationMillis = duration,
            startDelayMillis = startDelay
        )
    }
}