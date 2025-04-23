package com.example.robertosanchez.watchit.ui.screens.principalScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.example.robertosanchez.proyectoapi.data.AuthManager
import com.example.robertosanchez.watchit.data.model.Peliculas
import com.example.robertosanchez.watchit.ui.navegacion.BottomNavigationBar
import com.example.robertosanchez.watchit.ui.navegacion.BottomNavItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalScreen(popularesViewModel: PeliculasPopularesViewModel,
                    ratedViewModel: PeliculasRatedViewModel,
                    auth: AuthManager,
                    navigateToLogin: () -> Unit) {
    var showDialog by remember { mutableStateOf<DialogType?>(null) }
    val navController = rememberNavController()

    val lista_populares by popularesViewModel.lista.observeAsState(emptyList())
    val progressBar_populares by popularesViewModel.progressBar.observeAsState(false)

    val lista_rated by ratedViewModel.lista.observeAsState(emptyList())
    val progressBar_rated by ratedViewModel.progressBar.observeAsState(false)

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
                    // Peliculas Populares
                    if (progressBar_populares) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        if (lista_populares!!.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No hay elementos", style = MaterialTheme.typography.bodySmall)
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(lista_populares!!) { pelicula ->
                                    PeliculasListItem(pelicula)
                                }
                            }
                        }
                    }

                    // Peliculas Rated
                    if (progressBar_rated) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        if (lista_rated!!.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No hay elementos", style = MaterialTheme.typography.bodySmall)
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(lista_rated!!) { pelicula ->
                                    PeliculasListItem(pelicula)
                                }
                            }
                        }
                    }
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

@Composable
private fun PeliculasListItem(pelicula: Peliculas) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .padding(2.dp)
            .clickable { /**/ },
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Imagen(item = pelicula)
    }
}

@Composable
fun Imagen(item: Peliculas, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {

        Image(
            painter = rememberAsyncImagePainter(
                model = item.poster,
                imageLoader = ImageLoader.Builder(context).crossfade(true).build()
            ),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}