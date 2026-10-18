package com.example.friendlylines.therapist_app.ui.materials.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.core.ScrollArea
import com.composables.core.ScrollAreaState
import com.composables.core.Thumb
import com.composables.core.VerticalScrollbar
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesShapes
import com.example.friendlylines.theme.scaled
import com.example.shared.data.models.PatternItem

private const val GRID_ROW_ITEMS_NUMBER = 5
private val LIST_HORIZONTAL_PADDING = 16.dp
private val LIST_VERTICAL_PADDING = 8.dp
private val GRID_HORIZONTAL_SPACING = 30.dp
private val GRID_VERTICAL_SPACING = 20.dp
private val SCROLLBAR_SPACING = 48.dp
val SCROLLBAR_WIDTH = 4.dp
val SCROLLBAR_SHAPE = 2.dp

@Composable
fun PatternListArea(
    patterns: List<PatternItem>,
    gridState: LazyGridState,
    scrollAreaState: ScrollAreaState,
    onCreateClick: () -> Unit,
    onDeleteClick: (PatternItem) -> Unit,
    onPatternClick: (PatternItem) -> Unit = {},
    showAddPatternItem: Boolean = true,
    showBorder: Boolean = false,
    showDeleteBotton: Boolean = true,
    isPatternSelected: (PatternItem) -> Boolean = { false },
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ScrollArea(
            state = scrollAreaState,
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .then(
                            if (showBorder) {
                                Modifier
                                    .border(
                                        width = 2.dp.scaled(),
                                        color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                                        shape = FriendlyLinesShapes.extraSmall
                                    )
                            } else {
                                Modifier
                            }
                        )
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(GRID_ROW_ITEMS_NUMBER),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = LIST_HORIZONTAL_PADDING.scaled(),
                            vertical = LIST_VERTICAL_PADDING.scaled()
                        ),
                        horizontalArrangement = Arrangement.spacedBy(GRID_HORIZONTAL_SPACING.scaled()),
                        verticalArrangement = Arrangement.spacedBy(GRID_VERTICAL_SPACING.scaled()),
                        state = gridState
                    ) {
                        if (showAddPatternItem) {
                            item {
                                AddPatternItem(
                                    onCreateClick = onCreateClick
                                )
                            }
                        }

                        items(
                            items = patterns,
                            key = { it.pattern.id }
                        ) { pattern ->
                            if (showDeleteBotton) {
                                PatternItem(
                                    pattern = pattern,
                                    onDeleteClick = {
                                        onDeleteClick(pattern)
                                    }
                                )
                            } else {
                                PatternSelectionItem(
                                    pattern = pattern,
                                    selected = isPatternSelected(pattern),
                                    onClick = {
                                        onPatternClick(pattern)
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(SCROLLBAR_SPACING.scaled()))

                VerticalScrollbar(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .fillMaxHeight()
                        .width(SCROLLBAR_WIDTH.scaled())
                ) {
                    Thumb(
                        Modifier.background(
                            color = FriendlyLinesColors.PrimaryFriendlyLines.P700,
                            shape = RoundedCornerShape(SCROLLBAR_SHAPE.scaled())
                        )
                    )
                }
            }
        }
    }
}