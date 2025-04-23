package com.example.robertosanchez.watchit.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.robertosanchez.proyectoapi.data.AuthManager
import com.example.robertosanchez.proyectoapi.ui.screens.contrasenaOlvScreen.ContrasenaOlvScreen
import com.example.robertosanchez.proyectoapi.ui.screens.inicioScreen.InicioScreen
import com.example.robertosanchez.proyectoapi.ui.screens.loginScreen.LoginScreen
import com.example.robertosanchez.proyectoapi.ui.screens.registroScreen.RegistroScreen
import com.example.robertosanchez.watchit.ui.screens.principalScreen.PrincipalScreen
import com.example.robertosanchez.watchit.ui.screens.principalScreen.PeliculasPopularesViewModel
import com.example.robertosanchez.watchit.ui.screens.principalScreen.PeliculasRatedViewModel


@Composable
fun Navegacion(auth: AuthManager) {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Inicio) {
        composable<Inicio> {
            InicioScreen({
                    navController.navigate(Login)
                }
            )
        }

        composable<Login> {
            LoginScreen(
                auth,
                { navController.navigate(Registro) },
                {
                    navController.navigate(Principal) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                { navController.navigate(ContraseñaOlv) }
            )
        }

        composable<Registro> {
            RegistroScreen(
                auth
            ) { navController.popBackStack() }
        }

        composable<ContraseñaOlv> {
            ContrasenaOlvScreen(
                auth,
                { navController.navigate(Login) }
            )
        }

        composable<Principal> {
            val popularesViewModel = PeliculasPopularesViewModel()
            val ratedViewModel = PeliculasRatedViewModel()
            PrincipalScreen(
                popularesViewModel = popularesViewModel,
                ratedViewModel = ratedViewModel,
                auth = auth,
                navigateToLogin = { navController.navigate(Login) }
            )
        }
    }
}