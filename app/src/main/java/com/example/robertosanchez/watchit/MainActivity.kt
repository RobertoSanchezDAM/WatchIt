package com.example.robertosanchez.watchit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.robertosanchez.proyectoapi.data.AuthManager
import com.example.robertosanchez.watchit.ui.navegacion.Navegacion
import com.example.robertosanchez.watchit.ui.theme.WatchItTheme
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    val auth = AuthManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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