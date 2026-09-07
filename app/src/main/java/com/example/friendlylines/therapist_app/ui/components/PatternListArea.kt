package com.example.friendlylines.therapist_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.composables.core.ScrollAreaState
import com.example.friendlylines.therapist_app.ui.materials.models.PatternItem
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.composables.core.ScrollArea
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import com.composables.core.Thumb
import com.composables.core.VerticalScrollbar

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
                                        width = 2.dp,
                                        color = Primary900,
                                        shape = RoundedCornerShape(2.dp)
                                    )
                                    .padding(4.dp)
                            } else {
                                Modifier
                            }
                        )
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(5),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 12.dp,
                            end = 20.dp,
                            top = 8.dp,
                            bottom = 8.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(30.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp),
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

                Spacer(modifier = Modifier.width(48.dp))

                VerticalScrollbar(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .fillMaxHeight()
                        .width(4.dp)
                ) {
                    Thumb(
                        Modifier.background(
                            color = Primary700,
                            shape = RoundedCornerShape(2.dp)
                        )
                    )
                }
            }
        }
    }
}