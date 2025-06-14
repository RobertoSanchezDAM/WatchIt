package com.example.robertosanchez.watchinit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.robertosanchez.watchinit.data.AuthManager
import com.example.robertosanchez.watchinit.ui.navegacion.Navegacion
import com.example.robertosanchez.watchinit.ui.theme.WatchinItTheme
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    val auth = AuthManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Firebase.analytics
        setContent {
            WatchinItTheme {
                Navegacion(auth)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        auth.signOut()
    }
}