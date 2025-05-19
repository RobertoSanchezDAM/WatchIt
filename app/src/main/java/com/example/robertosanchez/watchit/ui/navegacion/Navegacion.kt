package com.example.robertosanchez.watchit.ui.navegacion

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.robertosanchez.watchit.data.AuthManager
import com.example.robertosanchez.watchit.db.FirestoreManager
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.BusquedaScreen
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.BusquedaViewModel
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.BusquedaViewModelFactory
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaNombreScreen.BusquedaNombreScreen
import com.example.robertosanchez.watchit.ui.screens.contrasenaOlvScreen.ContrasenaOlvScreen
import com.example.robertosanchez.watchit.ui.screens.inicioScreen.InicioScreen
import com.example.robertosanchez.watchit.ui.screens.detailScreen.DetailScreen
import com.example.robertosanchez.watchit.ui.screens.loginScreen.LoginScreen
import com.example.robertosanchez.watchit.ui.screens.perfilScreen.PerfilScreen
import com.example.robertosanchez.watchit.ui.screens.principalScreen.PrincipalScreen
import com.example.robertosanchez.watchit.ui.screens.principalScreen.PeliculasPopularesViewModel
import com.example.robertosanchez.watchit.ui.screens.principalScreen.PeliculasRatedViewModel
import com.example.robertosanchez.watchit.ui.screens.registroScreen.RegistroScreen
import com.example.robertosanchez.watchit.ui.screens.listaLargaPeliculas.ListaLargaPeliculasScreen
import com.example.robertosanchez.watchit.ui.screens.perfilScreen.PeliculasFavoritasViewModel
import com.example.robertosanchez.watchit.ui.screens.perfilScreen.PeliculasFavoritasViewModelFactory


@Composable
fun Navegacion(auth: AuthManager) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val popularesViewModel = remember { PeliculasPopularesViewModel() }
    val ratedViewModel = remember { PeliculasRatedViewModel() }

    val firestoreManager = FirestoreManager(auth, context)
    val peliculasFavoritasViewModel: PeliculasFavoritasViewModel = viewModel(
        factory = PeliculasFavoritasViewModelFactory(firestoreManager)
    )

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
                favoritasViewModel = peliculasFavoritasViewModel,
                auth = auth,
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                },
                navigateToLogin = { navController.navigate(Login) },
                navigateToListaLarga = { tipo ->
                    navController.navigate(ListaLargaPeliculas(tipo.name))
                },
                navigateToBusquedaNombre = { pelicula ->
                    navController.navigate(BusquedaNombreScreen(pelicula))
                }
            )
        }

        composable<ListaLargaPeliculas> { backStackEntry ->
            val seccion = backStackEntry.toRoute<ListaLargaPeliculas>()
            ListaLargaPeliculasScreen(
                popularesViewModel = popularesViewModel,
                ratedViewModel = ratedViewModel,
                favoritasViewModel = peliculasFavoritasViewModel,
                seccion = seccion.tipo,
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                },
                auth = auth,
                navigateToPrincipal = {
                    navController.navigate(Principal)
                },
                navigateToLogin = { navController.navigate(Login) },
            )
        }

        composable<Perfil> {
            PerfilScreen(
                auth = auth,
                anadirViewModel = peliculasFavoritasViewModel
            )
        }

        composable<Detail> { backStackEntry ->
            val detail = backStackEntry.toRoute<Detail>()
            val id = detail.id
            DetailScreen(
                id = id,
                popularesViewModel = popularesViewModel,
                ratedViewModel = ratedViewModel,
                navController = navController,
                anadirViewModel = peliculasFavoritasViewModel
            )
        }

        composable<Busqueda> {
            BusquedaScreen(
                auth = auth,
                navigateToBusquedaNombre = { pelicula ->
                    navController.navigate(BusquedaNombreScreen(pelicula))
                }
            )
        }

        composable<BusquedaNombreScreen> { backStackEntry ->
            val pelicula = backStackEntry.arguments?.getString("pelicula") ?: ""

            Log.d("Texto PELICULA PASAR", "$pelicula")

            val busquedaViewModel: BusquedaViewModel = viewModel(
                factory = BusquedaViewModelFactory(pelicula)
            )

            BusquedaNombreScreen(
                auth = auth,
                viewModel = busquedaViewModel,
                navigateToLogin = { navController.navigate(Login) },
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                },
                navigateToPrincipal = {
                    navController.navigate(Principal)
                }
            )
        }

    }
}