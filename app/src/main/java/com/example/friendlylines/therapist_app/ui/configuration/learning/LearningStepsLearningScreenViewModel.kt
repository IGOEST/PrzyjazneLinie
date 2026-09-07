package com.example.friendlylines.therapist_app.ui.configuration.learning

@HiltViewModel
class LearningStepsLearningScreenViewModel @Inject constructor() : ViewModel() {

    private val _draft = MutableStateFlow(
        LearningStepsDraft(id = 0)
    )

    val draft: StateFlow<LearningStepsDraft> = _draft.asStateFlow()

    fun setRepetitions(value: Int) {
        _draft.update { it.copy(repetitions = value) }
    }

    fun setAttempts(value: Int) {
        _draft.update { it.copy(attempts = value) }
    }

    fun setTimeLimit(value: Int) {
        _draft.update { it.copy(timeLimit = value) }
    }

    fun setAccuracyLevel(value: AccuracyLevel) {
        _draft.update { it.copy(accuracyLevel = value) }
    }

    fun setStartingPointEnabled(value: Boolean) {
        _draft.update { it.copy(startingPointEnabled = value) }
    }

    fun setRandomPatternOrder(value: Boolean) {
        _draft.update { it.copy(randomPatternOrder = value) }
    }
}