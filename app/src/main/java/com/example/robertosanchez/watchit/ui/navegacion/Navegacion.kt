package com.example.robertosanchez.watchit.ui.navegacion

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaEnCines.EnCineScreen
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaEnCines.EnCineViewModel
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.fechaScreen.ListaFechaScreen
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.generoScreen.ListaGeneroScreen
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.fechaScreen.FechaScreen
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.fechaScreen.FechaViewModel
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.fechaScreen.FechaViewModelFactory
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.generoScreen.GeneroScreen
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.generoScreen.GeneroViewModel
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.generoScreen.GeneroViewModelFactory
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaNombreScreen.BusquedaNombreScreen
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaProximosEstrenos.ProximosEstrenosScreen
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaProximosEstrenos.ProximosEstrenosViewModel
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
import com.example.robertosanchez.watchit.ui.screens.watchListScreen.WatchListScreen
import com.example.robertosanchez.watchit.ui.screens.watchListScreen.WatchListViewModel
import com.example.robertosanchez.watchit.ui.screens.watchListScreen.WatchListViewModelFactory


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navegacion(auth: AuthManager) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val popularesViewModel = remember { PeliculasPopularesViewModel() }
    val ratedViewModel = remember { PeliculasRatedViewModel() }
    val proximosEstrenosViewModel = remember { ProximosEstrenosViewModel() }
    val enCineViewModel = remember { EnCineViewModel() }

    val firestoreManager = FirestoreManager(auth, context)
    val peliculasFavoritasViewModel: PeliculasFavoritasViewModel = viewModel(
        factory = PeliculasFavoritasViewModelFactory(firestoreManager, auth)
    )
    val watchListViewModel: WatchListViewModel = viewModel(
        factory = WatchListViewModelFactory(firestoreManager, auth)
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
                firestoreManager,
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
                auth,
                firestoreManager
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
                firestore = firestoreManager,
                auth = auth,
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                },
                navigateToLogin = { navController.navigate(Login) },
                navigateToListaLarga = { tipo ->
                    navController.navigate(ListaLargaPeliculas(tipo.name))
                },
                navigateToBusquedaNombre = { pelicula ->
                    navController.navigate(BusquedaNombre(pelicula))
                },
                navigateToListaFecha = { navController.navigate(ListaFecha) },
                navigateToListaGenero = { navController.navigate(ListaGenero) },
                navigateToProximosEstrenos = { navController.navigate(ProximosEstrenos) },
                navigateToEnCine = { navController.navigate(EnCines) }
            )
        }

        composable<ListaLargaPeliculas> { backStackEntry ->
            val seccion = backStackEntry.toRoute<ListaLargaPeliculas>()
            ListaLargaPeliculasScreen(
                popularesViewModel = popularesViewModel,
                ratedViewModel = ratedViewModel,
                seccion = seccion.tipo,
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                },
                auth = auth,
                navigateBack = { navController.popBackStack() },
            )
        }

        composable<Perfil> {
            PerfilScreen(
                auth = auth,
                firestore = firestoreManager,
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                }
            )
        }

        composable<Detail> { backStackEntry ->
            val detail = backStackEntry.toRoute<Detail>()
            val id = detail.id
            DetailScreen(
                id = id,
                popularesViewModel = popularesViewModel,
                ratedViewModel = ratedViewModel,
                peliculasFavoritasViewModel = peliculasFavoritasViewModel,
                enCineViewModel = enCineViewModel,
                proximosEstrenosViewModel = proximosEstrenosViewModel,
                navigateBack = { navController.popBackStack() },
                auth = auth,
                watchListViewModel = watchListViewModel
            )
        }

        composable<Busqueda> {
            BusquedaScreen(
                auth = auth,
                navigateToBusquedaNombre = { pelicula ->
                    navController.navigate(BusquedaNombre(pelicula))
                },
                navigateToListaFecha = { navController.navigate(ListaFecha) },
                navigateToListaGenero = { navController.navigate(ListaGenero) },
                navigateToProximosEstrenos = { navController.navigate(ProximosEstrenos) },
                navigateToEnCines = {navController.navigate(EnCines)}
            )
        }

        composable<WatchList> {
            WatchListScreen(
                auth = auth,
                firestore = firestoreManager,
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                }
            )
        }

        composable<BusquedaNombre> { backStackEntry ->
            val pelicula = backStackEntry.arguments?.getString("pelicula") ?: ""

            val busquedaViewModel: BusquedaViewModel = viewModel(
                factory = BusquedaViewModelFactory(pelicula)
            )

            BusquedaNombreScreen(
                auth = auth,
                viewModel = busquedaViewModel,
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                },
                navigateBack = { navController.popBackStack() },
            )
        }

        composable<ListaFecha> {
            ListaFechaScreen(
                auth = auth,
                navigateToFecha = { fecha ->
                    navController.navigate(Fecha(fecha))
                },
                navigateBack = { navController.popBackStack() },
            )
        }

        composable<ListaGenero> {
            ListaGeneroScreen(
                auth = auth,
                navigateToBusquedaFecha = { genero ->
                    navController.navigate(Genero(genero))
                },
                navigateBack = { navController.popBackStack() },
            )
        }

        composable<Genero> { backStackEntry ->
            val detail = backStackEntry.toRoute<Genero>()
            val genero = detail.genero

            val generoViewModel: GeneroViewModel = viewModel(
                factory = GeneroViewModelFactory(genero)
            )

            GeneroScreen(
                auth = auth,
                viewModel = generoViewModel,
                navigateToDetail = { id -> navController.navigate(Detail(id)) },
                navigateBack = { navController.popBackStack() },
            )
        }

        composable<Fecha> { backStackEntry ->
            val detail = backStackEntry.toRoute<Fecha>()
            val fecha = detail.fecha

            val fechaViewModel: FechaViewModel = viewModel(
                factory = FechaViewModelFactory(fecha)
            )

            FechaScreen(
                auth = auth,
                viewModel = fechaViewModel,
                navigateToDetail = { id -> navController.navigate(Detail(id)) },
                navigateBack = { navController.popBackStack() },
            )
        }

        composable<ProximosEstrenos> {
            ProximosEstrenosScreen(
                viewModel = proximosEstrenosViewModel,
                auth = auth,
                navigateToDetail = { id -> navController.navigate(Detail(id)) },
                navigateBack = { navController.popBackStack() },
            )
        }

        composable<EnCines> {
            EnCineScreen(
                viewModel = enCineViewModel,
                auth = auth,
                navigateToDetail = { id -> navController.navigate(Detail(id)) },
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}