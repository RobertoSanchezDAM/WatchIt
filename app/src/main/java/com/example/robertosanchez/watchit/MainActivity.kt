package com.example.robertosanchez.watchit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.robertosanchez.proyectoapi.data.AuthManager
import com.example.robertosanchez.watchit.ui.navegacion.Navegacion
import com.example.robertosanchez.watchit.ui.theme.WatchItTheme
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    val auth = AuthManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        Firebase.analytics
        setContent {
            WatchItTheme {
                Navegacion(auth)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        auth.signOut()
    }
}