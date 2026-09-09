package com.example.friendlylines.therapist_app.ui.configuration.list

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import com.example.friendlylines.R
import com.example.friendlylines.therapist_app.ui.components.LearningStepListArea
import com.example.friendlylines.therapist_app.ui.components.TemplateButton
import com.example.friendlylines.therapist_app.ui.components.TemplateCheckbox
import com.example.friendlylines.therapist_app.ui.components.TemplateClickableIcon
import com.example.friendlylines.therapist_app.ui.components.TemplateInfoDialog
import com.example.friendlylines.therapist_app.ui.components.TemplateSearchBox
import com.example.friendlylines.therapist_app.ui.components.TemplateTopAppBar
import com.example.friendlylines.therapist_app.ui.theme.InfoActive
import com.example.friendlylines.therapist_app.ui.theme.InfoDefault
import com.example.friendlylines.therapist_app.ui.theme.Neutral300
import com.example.friendlylines.therapist_app.ui.theme.Primary50
import com.example.friendlylines.therapist_app.ui.theme.Primary700
import com.example.friendlylines.therapist_app.ui.theme.Primary900
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner

@Composable
fun LearningStepsListScreen(
    navController: NavController,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onCreateClick: () -> Unit,
    viewModel: LearningStepsListScreenViewModel = hiltViewModel()
) {
    var searchQuery by remember {
        mutableStateOf("")
    }

    var showInfoDialog by remember {
        mutableStateOf(false)
    }

    var hideExampleSteps by remember {
        mutableStateOf(false)
    }

    val learningSteps by viewModel.learningSteps.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadLearningSteps()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        containerColor = Primary50,
        topBar = {
            TemplateTopAppBar(
                text = stringResource(R.string.learning_steps_header),
                onBackClick = onBackClick,
                onHomeClick = onHomeClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LearningStepsListToolbar(
                searchQuery = searchQuery,
                onSearchQueryChange = {
                    searchQuery = it
                },
                onCreateClick = onCreateClick,
                onInfoClick = {
                    showInfoDialog = true
                }
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = Primary700
            )

            LearningStepsListHeader(
                hideExampleSteps = hideExampleSteps,
                onHideExampleStepsChange = {
                    hideExampleSteps = it
                }
            )

            LearningStepListArea(
                learningSteps = learningSteps,
                modifier = Modifier.weight(1f),
                onDeleteClick = { step ->
                    // later
                },
                onActiveStepClick = { step ->
                    viewModel.setActiveStep(step.id)
                },
                onModeChange = { step, isTest ->
                    viewModel.setMode(
                        stepId = step.id,
                        isTest = isTest
                    )
                },
                onCreateClick = onCreateClick
            )
        }

        if (showInfoDialog) {
            TemplateInfoDialog(
                title = stringResource(R.string.info_dialog_title),
                info = stringResource(R.string.learning_steps_info),
                onDismiss = {
                    showInfoDialog = false
                }
            )
        }
    }
}

@Composable
fun LearningStepsListToolbar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onCreateClick: () -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TemplateSearchBox (
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = stringResource(R.string.search_box_text),
            modifier = Modifier.weight(0.8f)
        )

        Spacer(modifier = Modifier.width(24.dp))

        TemplateClickableIcon(
            icon = Icons.Default.Info,
            contentDescription = null,
            onClick = {
                onInfoClick()
            },
            modifier = Modifier.size(24.dp),
            defaultTint = InfoDefault,
            activeTint = InfoActive,
            disabledTint = Neutral300
        )

        Spacer(modifier = Modifier.weight(1f))

        TemplateButton(
            modifier = Modifier
                .width(170.dp)
                .height(54.dp),
            enabled = true,
            onClick = onCreateClick,
            text = stringResource(R.string.create_button_text),
            icon = Icons.Outlined.AddCircle
        )
    }
}

@Composable
fun LearningStepsListHeader(
    hideExampleSteps: Boolean,
    onHideExampleStepsChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 32.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(R.string.steps_list_header_name),
                fontSize = 16.7.sp,
                fontWeight = FontWeight.Normal,
                color = Primary900
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.hide_exemplary_steps),
                fontSize = 16.7.sp,
                fontWeight = FontWeight.Normal,
                color = Primary900
            )

            Spacer(modifier = Modifier.width(8.dp))

            TemplateCheckbox(
                checked = hideExampleSteps,
                onCheckedChange = onHideExampleStepsChange,
                modifier = Modifier.size(18.dp)
            )
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = stringResource(R.string.list_header_actions),
                fontSize = 16.7.sp,
                fontWeight = FontWeight.Normal,
                color = Primary900
            )
        }
    }
}