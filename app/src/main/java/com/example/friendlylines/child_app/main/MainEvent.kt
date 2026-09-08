package com.example.friendlylines.child_app.main

sealed class ChildMainEvent {
    object GoToNextScreen : ChildMainEvent()
    object BackToMainMenu : ChildMainEvent()
    //tutaj updaty wartości do wyników
}
