package pg.autyzm.friendlyemotions.child.backgrounds

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.friendlylines.R
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.scaled

private const val FLOOR_HEIGHT_FRACTION = 0.2f

@Composable
fun ChildGameStartBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {},
) {
    BoxWithConstraints(
        modifier =
            modifier
                .fillMaxSize()
                .background(FriendlyLinesColors.PrimaryFriendlyLines.P300),
    ) {
        val floorHeight = maxHeight * FLOOR_HEIGHT_FRACTION

        Box(
            modifier =
                Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(floorHeight)
                    .background(FriendlyLinesColors.PrimaryFriendlyLines.P500),
        )

        Image(
            painter = painterResource(R.drawable.rybka3),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(
                    end = 80.dp.scaled(),
                    bottom = floorHeight - 20.dp.scaled(),
                )
                .size(200.dp.scaled()),
        )

        content()
    }
}
