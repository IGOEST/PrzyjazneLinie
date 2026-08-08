package com.example.friendlylines.child_app.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.friendlylines.R
import com.example.friendlylines.theme.AppTextStyles
import com.example.friendlylines.therapist_app.main.TherapistActivity

class ChildActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            setContent {
                MaterialTheme {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = stringResource(R.string.title_child),
                                style = AppTextStyles.h3Medium,
                                color = colorResource(R.color.green_1000))
                        }
                    }
                }

                val context = LocalContext.current
                Button(onClick = {
                    val intent = Intent(context, TherapistActivity::class.java)
                    context.startActivity(intent)
                }) {
                    Text(text = stringResource(R.string.change_app_DEVEL))
                }
            }
        }
    }
}