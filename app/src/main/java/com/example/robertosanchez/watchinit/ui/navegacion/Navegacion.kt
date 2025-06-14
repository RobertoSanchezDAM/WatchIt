package com.example.robertosanchez.watchinit.ui.navegacion

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.robertosanchez.watchinit.data.AuthManager
import com.example.robertosanchez.watchinit.db.FirestoreManager
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.BusquedaScreen
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.BusquedaViewModel
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.BusquedaViewModelFactory
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaEnCines.EnCineScreen
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaEnCines.EnCineViewModel
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.fechaScreen.ListaFechaScreen
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.generoScreen.ListaGeneroScreen
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.fechaScreen.FechaScreen
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.fechaScreen.FechaViewModel
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.fechaScreen.FechaViewModelFactory
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.generoScreen.GeneroScreen
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.generoScreen.GeneroViewModel
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.generoScreen.GeneroViewModelFactory
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaNombreScreen.BusquedaNombreScreen
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaProximosEstrenos.ProximosEstrenosScreen
import com.example.robertosanchez.watchinit.ui.screens.busquedaScreen.busquedaProximosEstrenos.ProximosEstrenosViewModel
import com.example.robertosanchez.watchinit.ui.screens.contrasenaOlvScreen.ContrasenaOlvScreen
import com.example.robertosanchez.watchinit.ui.screens.inicioScreen.InicioScreen
import com.example.robertosanchez.watchinit.ui.screens.detailScreen.DetailScreen
import com.example.robertosanchez.watchinit.ui.screens.loginScreen.LoginScreen
import com.example.robertosanchez.watchinit.ui.screens.perfilScreen.PerfilScreen
import com.example.robertosanchez.watchinit.ui.screens.principalScreen.PrincipalScreen
import com.example.robertosanchez.watchinit.ui.screens.principalScreen.PeliculasPopularesViewModel
import com.example.robertosanchez.watchinit.ui.screens.principalScreen.PeliculasRatedViewModel
import com.example.robertosanchez.watchinit.ui.screens.registroScreen.RegistroScreen
import com.example.robertosanchez.watchinit.ui.screens.listaLargaPeliculas.ListaLargaPeliculasScreen
import com.example.robertosanchez.watchinit.ui.screens.peliculasVistasScreen.PeliculasVistasScreen
import com.example.robertosanchez.watchinit.ui.screens.peliculasVistasScreen.PeliculasVistasViewModel
import com.example.robertosanchez.watchinit.ui.screens.peliculasVistasScreen.PeliculasVistasViewModelFactory
import com.example.robertosanchez.watchinit.ui.screens.perfilScreen.PeliculasFavoritasViewModel
import com.example.robertosanchez.watchinit.ui.screens.perfilScreen.PeliculasFavoritasViewModelFactory
import com.example.robertosanchez.watchinit.ui.screens.reviewsUsuarioScreen.ReviewsUsuarioScreen
import com.example.robertosanchez.watchinit.ui.screens.watchListScreen.WatchListScreen
import com.example.robertosanchez.watchinit.ui.screens.watchListScreen.WatchListViewModel
import com.example.robertosanchez.watchinit.ui.screens.watchListScreen.WatchListViewModelFactory
import com.example.robertosanchez.watchinit.ui.screens.perfilUsuarioSelectScreen.PerfilUsuarioSelectScreen
import androidx.navigation.navArgument


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navegacion(auth: AuthManager) {
    val navController = rememberNavController()
    val context = LocalContext.current

    // ViewModels de distintas secciones de películas
    val popularesViewModel = remember { PeliculasPopularesViewModel() }
    val ratedViewModel = remember { PeliculasRatedViewModel() }
    val proximosEstrenosViewModel = remember { ProximosEstrenosViewModel() }
    val enCineViewModel = remember { EnCineViewModel() }

    val firestoreManager = FirestoreManager(auth, context)

    // ViewModel Favoritas
    val peliculasFavoritasViewModel: PeliculasFavoritasViewModel = viewModel(
        factory = PeliculasFavoritasViewModelFactory(firestoreManager, auth)
    )

    // ViewModel Watch List
    val watchListViewModel: WatchListViewModel = viewModel(
        factory = WatchListViewModelFactory(firestoreManager, auth)
    )

    // ViewModel Vistas
    val peliculasVistasViewModel: PeliculasVistasViewModel = viewModel(
        factory = PeliculasVistasViewModelFactory(firestoreManager, auth)
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
                { navController.navigate(ContraseñaOlv) },
                { navController.navigate(Inicio) }
            )
        }

        composable<Registro> {
            RegistroScreen(
                auth,
                firestoreManager,
            ) { navController.popBackStack() }
        }

        composable<ContraseñaOlv> {
            ContrasenaOlvScreen(
                auth,
                { navController.navigate(Login) },
                onNavigateBack = { navController.popBackStack() }
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
                navigateToEnCine = { navController.navigate(EnCines) },
                navigateToVistas = { navController.navigate(PeliculasVistas) },
                navigateToReviews = { navController.navigate(ReviewsUsuario) }
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
                navigateToPrincipal = { navController.navigate(Principal) }
            )
        }

        composable<Perfil> {
            PerfilScreen(
                auth = auth,
                firestore = firestoreManager,
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                },
                navigateToVistas = { navController.navigate(PeliculasVistas) },
                navigateToReviews = { navController.navigate(ReviewsUsuario) }
            )
        }

        composable<PeliculasVistas> {
            PeliculasVistasScreen(
                auth = auth,
                firestore = firestoreManager,
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                },
                navigateBack = { navController.popBackStack() },
                navigateToPrincipal = { navController.navigate(Principal) }
            )
        }

        composable<ReviewsUsuario> {
            ReviewsUsuarioScreen(
                auth = auth,
                firestore = firestoreManager,
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                },
                navigateBack = { navController.popBackStack() },
                navigateToPrincipal = { navController.navigate(Principal) }
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
                watchListViewModel = watchListViewModel,
                peliculasVistasViewModel = peliculasVistasViewModel,
                navigateToPerfilUsuario = { userId, userName, userPhotoUrl ->
                    navController.navigate(PerfilUsuarioSelect(userId, userName, userPhotoUrl))
                },
                navigateToPrincipal = { navController.navigate(Principal) }
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
                navigateToPrincipal = { navController.navigate(Principal) }
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
                navigateToPrincipal = { navController.navigate(Principal) }
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
                navigateToPrincipal = { navController.navigate(Principal) }
            )
        }

        composable<ProximosEstrenos> {
            ProximosEstrenosScreen(
                viewModel = proximosEstrenosViewModel,
                auth = auth,
                navigateToDetail = { id -> navController.navigate(Detail(id)) },
                navigateBack = { navController.popBackStack() },
                navigateToPrincipal = { navController.navigate(Principal) }
            )
        }

        composable<EnCines> {
            EnCineScreen(
                viewModel = enCineViewModel,
                auth = auth,
                navigateToDetail = { id -> navController.navigate(Detail(id)) },
                navigateBack = { navController.popBackStack() },
                navigateToPrincipal = { navController.navigate(Principal) }
            )
        }

        composable<PerfilUsuarioSelect> { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            val userName = backStackEntry.arguments?.getString("userName") ?: ""
            val userPhotoUrl = backStackEntry.arguments?.getString("userPhotoUrl")
            
            PerfilUsuarioSelectScreen(
                userId = userId,
                userName = userName,
                userPhotoUrl = userPhotoUrl,
                auth = auth,
                firestore = firestoreManager,
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                },
                navigateBack = { navController.popBackStack() },
                navigateToVistas = { navController.navigate("peliculas_vistas/$userId") },
                navigateToReviews = { navController.navigate("reviews_usuario/$userId") },
                navigateToPrincipal = { navController.navigate(Principal) }
            )
        }

        composable(
            route = "peliculas_vistas/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            PeliculasVistasScreen(
                auth = auth,
                firestore = firestoreManager,
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                },
                navigateBack = { navController.popBackStack() },
                userId = userId,
                navigateToPrincipal = { navController.navigate(Principal) }
            )
        }

        composable(
            route = "reviews_usuario/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            ReviewsUsuarioScreen(
                auth = auth,
                firestore = firestoreManager,
                navigateToDetail = { id ->
                    navController.navigate(Detail(id))
                },
                navigateBack = { navController.popBackStack() },
                userId = userId,
                navigateToPrincipal = { navController.navigate(Principal) }
            )
        }
    }
}