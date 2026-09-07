package com.example.friendlylines.therapist_app.ui.materials.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.friendlylines.therapist_app.ui.components.TemplateCheckbox
import com.example.friendlylines.therapist_app.ui.components.TemplateNotification
import com.example.friendlylines.therapist_app.ui.components.TemplateTopAppBar
//import com.example.friendlylines.therapist_app.ui.materials.models.PatternItem
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import com.example.friendlylines.therapist_app.ui.theme.Primary900
import com.example.shared.data.models.PatternItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onCreateClick: () -> Unit
) {
    val viewModel: GalleryScreenViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

//    LaunchedEffect(Unit) {
//        viewModel.onEvent(GalleryScreenEvent.Initialize)
//    }

//    val viewModel: GalleryScreenViewModel = hiltViewModel()
//
//    LaunchedEffect(Unit) {
//        viewModel.initializePatterns()
//    }
//
//    var hideExamplePatterns by remember {
//        mutableStateOf(false)
//    }
//
//    val patternItems by viewModel.patterns.collectAsStateWithLifecycle()

    val gridState = rememberLazyGridState()
    val scrollAreaState = rememberScrollAreaState(gridState)

    val visiblePatterns = if (state.hideExamplePatterns) {
        state.patterns.filter { !it.pattern.isExample }
    } else {
        state.patterns
    }

//    val visiblePatterns = if (hideExamplePatterns) {
//        patternItems.filter {!it.pattern.isExample}
//    } else {
//        patternItems
//    }
//
//    var patternToDelete by remember {
//        mutableStateOf<PatternItem?>(null)
//    }

//    var scrollToPatternId by remember {
//        mutableStateOf<Long?>(null)
//    }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val savedStateHandle = navController.currentBackStackEntry
        ?.savedStateHandle

    val newPatternId = savedStateHandle
        ?.getStateFlow<Long?>(
            "newPatternId",
            null
        )
        ?.collectAsState()
        ?.value

    val savedPopUpMessage = stringResource(R.string.saved_pop_up_message)
    LaunchedEffect(newPatternId) {
        if (newPatternId != null) {
            viewModel.onEvent(
                GalleryScreenEvent.NewPatternReceived(newPatternId)
            )
//            scrollToPatternId = newPatternId

            snackbarHostState.showSnackbar(
                message = savedPopUpMessage,
                duration = SnackbarDuration.Short
            )

            savedStateHandle?.remove<Long>("newPatternId")
        }
    }

    LaunchedEffect(state.scrollToPatternId, visiblePatterns) {
        val patternId = state.scrollToPatternId ?: return@LaunchedEffect

        val patternIndex = visiblePatterns.indexOfFirst {
            it.pattern.id == patternId
        }

        if (patternIndex >= 0) {
            val gridIndex = patternIndex + 1

            gridState.animateScrollToItem(
                index = gridIndex
            )
//            scrollToPatternId = null
        }

        viewModel.onEvent(
            GalleryScreenEvent.ScrollToPattern
        )
    }

    Scaffold(
        containerColor = Primary50,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) { snackbarData ->
                TemplateNotification(
                    snackbarData = snackbarData
                )
            }
        },
        topBar = {
            TemplateTopAppBar(
                text = stringResource(R.string.pattern_gallery_header),
                onBackClick = onBackClick,
                onHomeClick = onHomeClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Primary50)
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.hide_exemplary_patterns),
                    fontSize = 16.7.sp,
                    color = Primary900,
                    modifier = Modifier.padding(start = 20.dp),
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.width(8.dp))

                TemplateCheckbox(
                    checked = state.hideExamplePatterns,
                    onCheckedChange = {
                        //hideExamplePatterns = it
                        viewModel.onEvent(
                            GalleryScreenEvent.HideExamplePatternsChanged(it)
                        )
                    },
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            PatternListArea(
                patterns = visiblePatterns,
                gridState = gridState,
                scrollAreaState = scrollAreaState,
                onCreateClick = onCreateClick,
                onDeleteClick = {
                    //patternToDelete = it
                    viewModel.onEvent(
                        GalleryScreenEvent.DeletePatternClicked(it)
                    )
                },
                showAddPatternItem = true,
                showBorder = false,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .weight(1f)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }

    if (state.patternToDelete != null) {
        TemplateAlertDialog(
            title = stringResource(R.string.delete_dialog_title),
            message = stringResource(R.string.delete_pattern_dialog_message),
            confirmText = stringResource(R.string.delete_confirm_button_text),
            dismissText = stringResource(R.string.dismiss_button_text),
            onConfirm = {
                viewModel.onEvent(
                    GalleryScreenEvent.DeletePatternConfirmed
                )
//                patternToDelete?.let { pattern ->
//                    viewModel.deletePattern(pattern.pattern.id)
//                }
//                patternToDelete = null
            },
            onDismiss = {
                viewModel.onEvent(
                    GalleryScreenEvent.DeletePatternDismissed
                )
//                patternToDelete = null
            }
        )
    }
}