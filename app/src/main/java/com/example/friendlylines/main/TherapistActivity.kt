package com.example.friendlylines.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.friendlylines.child_app.main.ChildActivity
import com.example.friendlylines.theme.FriendlyLinesTheme
import com.example.friendlylines.therapist_app.navigation.TherapistNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TherapistActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FriendlyLinesTheme {
                TherapistNavGraph(onPlayRequested = { startActivity(Intent(this, ChildActivity::class.java)) })
            }
        }
    }
}