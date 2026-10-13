package com.example.friendlylines.therapist_app.ui.main

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class FriendlyLinesApp: Application() {
    @Inject
    lateinit var exampleDataInitializer: ExampleDataInitializer

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        applicationScope.launch { exampleDataInitializer.initialize() }
    }
}