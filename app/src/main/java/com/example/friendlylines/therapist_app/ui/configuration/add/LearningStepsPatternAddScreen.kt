package com.example.friendlylines.therapist_app.ui.configuration.add

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.composables.core.rememberScrollAreaState
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.components.PatternListArea
import com.example.friendlylines.therapist_app.ui.components.TemplateAlertDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateTopAppBar
import com.example.friendlylines.therapist_app.ui.main.ExitDestination
import com.example.friendlylines.therapist_app.ui.materials.gallery.GalleryScreenEvent
import com.example.friendlylines.therapist_app.ui.materials.gallery.GalleryScreenViewModel
//import com.example.friendlylines.therapist_app.ui.materials.models.PatternItem
import com.example.friendlylines.therapist_app.ui.theme.Primary1000
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import  com.example.shared.data.models.PatternItem

@Composable
fun LearningStepsPatternAddScreen(
    navController: NavController,
    configId: Long?,
    selectedPatternId: Long?,
    order: Int?,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onNextClick: (Long) -> Unit
) {
    val patternNumber = (order ?: 0) + 1

    val galleryViewModel: GalleryScreenViewModel = hiltViewModel()

    val gridState = rememberLazyGridState()
    val scrollAreaState = rememberScrollAreaState(gridState)

//    LaunchedEffect(Unit) {
//        galleryViewModel.initializePatterns()
//    }

    val galleryState by galleryViewModel.state.collectAsStateWithLifecycle()
//    val patterns by galleryViewModel.patterns.collectAsStateWithLifecycle()

    var selectedPattern by remember {
        mutableStateOf<PatternItem?>(null)
    }

    LaunchedEffect(galleryState.patterns, selectedPatternId) {
        if (selectedPatternId != null) {
            selectedPattern = galleryState.patterns.find {
                it.pattern.id == selectedPatternId
            }
        }
    }

//    LaunchedEffect(patterns, selectedPatternId) {
//        if (selectedPatternId != null) {
//            selectedPattern = patterns.find {
//                it.pattern.id == selectedPatternId
//            }
//        }
//    }

    var showExitDialog by remember {
        mutableStateOf(false)
    }

    var exitDestination by remember {
        mutableStateOf<ExitDestination?>(null)
    }

    BackHandler {
        showExitDialog = true
        exitDestination = ExitDestination.PREVIOUS
    }

    Scaffold(
        containerColor = Primary50,
        topBar = {
            TemplateTopAppBar(
                text = stringResource(R.string.pattern_text) + " " + "$patternNumber",
                onBackClick = {
                    showExitDialog = true
                    exitDestination = ExitDestination.PREVIOUS
                },
                onHomeClick = {
                    showExitDialog = true
                    exitDestination = ExitDestination.HOME
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(
                    horizontal = 32.dp,
                    vertical = 16.dp
                )
        ) {
            Text(
                text = stringResource(R.string.select_pattern_text),
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                color = Primary1000
            )

            Spacer(modifier = Modifier.height(16.dp))

            PatternListArea(
                patterns = galleryState.patterns,
                gridState = gridState,
                scrollAreaState = scrollAreaState,
                onCreateClick = {},
                onDeleteClick = {},
                onPatternClick = { pattern ->
                    selectedPattern = pattern
                },
                showAddPatternItem = false,
                showBorder = true,
                showDeleteBotton = false,
                isPatternSelected = { pattern ->
                    selectedPattern?.pattern?.id == pattern.pattern.id
                },
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .fillMaxHeight(1f)
                    .weight(1f)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.selected_pattern_text) + " " + (selectedPattern?.pattern?.name ?: "-"),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = Primary1000,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.weight(1f))

                TemplateButton(
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(54.dp),
                    enabled = selectedPattern != null,
                    onClick = {
                        selectedPattern?.let {
                            onNextClick(it.pattern.id)
                        }
                    },
                    text = stringResource(R.string.next_button_text),
                    icon = Icons.Default.ArrowForwardIos
                )
            }
        }
    }

    if (showExitDialog) {
        TemplateAlertDialog(
            title = stringResource(R.string.exit_dialog_title),
            message = stringResource(R.string.exit_dialog_message),
            confirmText = stringResource(R.string.exit_confirm_button_text),
            dismissText = stringResource(R.string.dismiss_button_text),
            onConfirm = {
                showExitDialog = false
                when (exitDestination) {
                    ExitDestination.PREVIOUS -> {
                        onBackClick()
                    }
                    ExitDestination.HOME -> {
                        onHomeClick()
                    }
                    null -> Unit
                }
                exitDestination = null
            },
            onDismiss = {
                showExitDialog = false
                exitDestination = null
            }
        )
    }
}