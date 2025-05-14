package com.example.robertosanchez.watchit.ui.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.robertosanchez.proyectoapi.data.AuthManager
import com.example.robertosanchez.proyectoapi.ui.screens.contrasenaOlvScreen.ContrasenaOlvScreen
import com.example.robertosanchez.proyectoapi.ui.screens.inicioScreen.InicioScreen
import com.example.robertosanchez.proyectoapi.ui.screens.loginScreen.LoginScreen
import com.example.robertosanchez.proyectoapi.ui.screens.registroScreen.RegistroScreen
import com.example.robertosanchez.watchit.ui.screens.detailScreen.DetailScreen
import com.example.robertosanchez.watchit.ui.screens.perfilScreen.PerfilScreen
import com.example.robertosanchez.watchit.ui.screens.principalScreen.PrincipalScreen
import com.example.robertosanchez.watchit.ui.screens.principalScreen.PeliculasPopularesViewModel
import com.example.robertosanchez.watchit.ui.screens.principalScreen.PeliculasRatedViewModel


@Composable
fun Navegacion(auth: AuthManager) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val popularesViewModel = remember { PeliculasPopularesViewModel() }
    val ratedViewModel = remember { PeliculasRatedViewModel() }

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
            PrincipalScreen(
                popularesViewModel = popularesViewModel,
                ratedViewModel = ratedViewModel,
                auth = auth,
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                },
                navigateToLogin = { navController.navigate(Login) }
            )
        }

        composable<Perfil> {
            PerfilScreen(
                auth = auth
            )
        }

        composable<Detail> { backStackEntry ->
            val detail = backStackEntry.toRoute<Detail>()
            val id = detail.id
            DetailScreen(
                id = id,
                popularesViewModel = popularesViewModel,
                ratedViewModel = ratedViewModel,
                navController = navController
            )
        }
    }
}