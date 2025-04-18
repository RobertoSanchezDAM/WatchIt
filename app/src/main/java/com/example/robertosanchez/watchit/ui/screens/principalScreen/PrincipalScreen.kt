package com.example.robertosanchez.watchit.ui.screens.principalScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.robertosanchez.proyectoapi.data.AuthManager
import com.example.robertosanchez.watchit.ui.navegacion.BottomNavigationBar
import com.example.robertosanchez.watchit.ui.navegacion.BottomNavItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalScreen(auth: AuthManager, navigateToLogin: () -> Unit) {
    var showDialog by remember { mutableStateOf<DialogType?>(null) }
    val navController = rememberNavController()
    val user = auth.getCurrentUser()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                onLogoutClick = { showDialog = DialogType.Logout }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route
            ) {
                composable(BottomNavItem.Home.route) {
                    PrincipalContenido()
                }
                composable(BottomNavItem.Search.route) {
                    /*SearchContent()*/
                }
                composable(BottomNavItem.Watch.route) {
                    /*WatchlistContent()*/
                }
                composable(BottomNavItem.Profile.route) {
                    /*ProfileContent()*/
                }
            }
        }

        when (showDialog) {
            DialogType.Logout -> {
                LogoutDialog(
                    onDismiss = { showDialog = null },
                    onConfirm = {
                        auth.signOut()
                        navigateToLogin()
                    }
                )
            }
            else -> Unit
        }
    }
}

@Composable
fun PrincipalContenido() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF272B30)),
        contentAlignment = Alignment.Center
    ) {
        Text("Pantalla de Inicio.", color = Color.White)
    }
}

/*@Composable
fun SearchContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF191B1F)),
        contentAlignment = Alignment.Center
    ) {
        Text("Pantalla de Búsqueda", color = Color.White)
    }
}

@Composable
fun WatchlistContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF191B1F)),
        contentAlignment = Alignment.Center
    ) {
        Text("Mi Lista", color = Color.White)
    }
}

@Composable
fun ProfileContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF191B1F)),
        contentAlignment = Alignment.Center
    ) {
        Text("Perfil", color = Color.White)
    }
}*/

// Enum para los tipos de diálogos
enum class DialogType {
    Logout
}

@Composable
fun LogoutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF2196F3),
        title = { Text("Cerrar Sesión", color = Color.Black) },
        text = { Text("¿Estás seguro de que deseas cerrar sesión?", color = Color.Black) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Text("Aceptar", color = Color.White)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Text("Cancelar", color = Color.White)
            }
        }
    )
}