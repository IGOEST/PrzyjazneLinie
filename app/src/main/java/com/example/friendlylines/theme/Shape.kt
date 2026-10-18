package com.example.friendlylines.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val FriendlyLinesShapes =
    Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(8.dp),
        medium = RoundedCornerShape(16.dp),
        large = RoundedCornerShape(24.dp),
        extraLarge = RoundedCornerShape(32.dp),
    )

val FriendlyLinesModalShape = RoundedCornerShape(10.dp)