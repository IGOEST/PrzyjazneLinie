package com.example.friendlylines.therapist_app.ui.materials.gallery

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.composables.core.rememberScrollAreaState
import com.example.friendlylines.R
import com.example.friendlylines.components.LoadingScreen
import com.example.friendlylines.theme.FriendlyLinesColors
import com.example.friendlylines.theme.FriendlyLinesTextStyles
import com.example.friendlylines.theme.scaled
import com.example.friendlylines.therapist_app.backgrounds.PlainBackground
import com.example.friendlylines.therapist_app.navigation.TemplateTopAppBar
import com.example.friendlylines.therapist_app.ui.components.InfoIconButton
import com.example.friendlylines.therapist_app.ui.components.TemplateAlertDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateCheckbox
import com.example.friendlylines.therapist_app.ui.components.TemplateNotification
import com.example.friendlylines.therapist_app.ui.materials.components.PatternListArea

private val CONTENT_PADDING = 20.dp

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

    val gridState = rememberLazyGridState()
    val scrollAreaState = rememberScrollAreaState(gridState)

    val visiblePatterns = if (state.hideExamplePatterns) {
        state.patterns.filter { !it.pattern.isExample }
    } else {
        state.patterns
    }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

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
        }

        viewModel.onEvent(
            GalleryScreenEvent.ScrollToPattern
        )
    }

    BackHandler {
        onBackClick()
    }

    Scaffold(
        containerColor = FriendlyLinesColors.PrimaryFriendlyLines.P50,
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
                onHomeClick = onHomeClick,
                infoTitle = stringResource(R.string.info_dialog_title),
                infoMessage = stringResource(R.string.pattern_gallery_screen_info_message)
            )
        }
    ) { padding ->
        val content: @Composable () -> Unit = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FriendlyLinesColors.PrimaryFriendlyLines.P50)
                    .padding(CONTENT_PADDING.scaled()),
                verticalArrangement = Arrangement.spacedBy(4.dp.scaled())
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp.scaled()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp.scaled())
                ) {
                    TemplateCheckbox(
                        checked = state.hideExamplePatterns,
                        onCheckedChange = {
                            viewModel.onEvent(
                                GalleryScreenEvent.HideExamplePatternsChanged(it)
                            )
                        },
                        modifier = Modifier.size(24.dp.scaled())
                    )
                    Text(
                        text = stringResource(R.string.hide_exemplary_patterns),
                        color = FriendlyLinesColors.PrimaryFriendlyLines.P900,
                        style = FriendlyLinesTextStyles.captionC1
                    )
                    InfoIconButton(
                        infoTitle = stringResource(R.string.info_dialog_title),
                        infoMessage = stringResource(R.string.hide_exemplary_patterns_info_message),
                        defaultTint = FriendlyLinesColors.InfoIcon.InfoDefault,
                        activeTint = FriendlyLinesColors.InfoIcon.InfoActive,
                        disabledTint = FriendlyLinesColors.Neutral.N300
                    )
                }

                PatternListArea(
                    patterns = visiblePatterns,
                    gridState = gridState,
                    scrollAreaState = scrollAreaState,
                    onCreateClick = onCreateClick,
                    onDeleteClick = {
                        viewModel.onEvent(
                            GalleryScreenEvent.DeletePatternClicked(it)
                        )
                    },
                    showAddPatternItem = true,
                    showBorder = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }

        if (state.isLoading) {
            PlainBackground(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
            ) { LoadingScreen() }
        } else {
            PlainBackground(modifier = Modifier
                .fillMaxSize()
                .padding(padding)
            ) { content() }
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
            },
            onDismiss = {
                viewModel.onEvent(
                    GalleryScreenEvent.DeletePatternDismissed
                )
            }
        )
    }
}