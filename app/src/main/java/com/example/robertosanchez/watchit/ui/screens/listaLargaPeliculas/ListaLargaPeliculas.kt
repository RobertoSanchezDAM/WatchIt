package com.example.robertosanchez.watchit.ui.screens.listaLargaPeliculas

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.robertosanchez.watchit.R
import com.example.robertosanchez.watchit.data.AuthManager
import com.example.robertosanchez.watchit.data.model.MediaItem
import com.example.robertosanchez.watchit.ui.navegacion.BottomNavItem
import com.example.robertosanchez.watchit.ui.navegacion.BottomNavigationBar import com.example.robertosanchez.watchit.ui.screens.perfilScreen.PeliculasFavoritasViewModel
import com.example.robertosanchez.watchit.ui.screens.perfilScreen.PerfilScreen
import com.example.robertosanchez.watchit.ui.screens.principalScreen.DialogType
import com.example.robertosanchez.watchit.ui.screens.principalScreen.PeliculasPopularesViewModel
import com.example.robertosanchez.watchit.ui.screens.principalScreen.PeliculasRatedViewModel
import com.example.robertosanchez.watchit.ui.screens.principalScreen.SeccionType
import com.example.robertosanchez.watchit.ui.shapes.BottomBarCustomShape
import com.example.robertosanchez.watchit.ui.shapes.CustomShape

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListaLargaPeliculasScreen(
    popularesViewModel: PeliculasPopularesViewModel,
    ratedViewModel: PeliculasRatedViewModel,
    favoritasViewModel: PeliculasFavoritasViewModel,
    seccion: String,
    navigateToDetail: (Int) -> Unit,
    auth: AuthManager,
    navigateToPrincipal: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    val user = auth.getCurrentUser()
    var showDialog by remember { mutableStateOf<DialogType?>(null) }
    val navController = rememberNavController()

    val lista_populares by popularesViewModel.lista.observeAsState(emptyList())
    val progressBar_populares by popularesViewModel.progressBar.observeAsState(false)

    val lista_rated by ratedViewModel.lista.observeAsState(emptyList())
    val progressBar_rated by ratedViewModel.progressBar.observeAsState(false)


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (seccion) {
                                "Populares" -> "Películas Populares esta Semana"
                                "Rated" -> "Películas mejor Valoradas"
                                else -> "Lista de Películas"
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black.copy(alpha = 0.8f),
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        if (user?.photoUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user.photoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(top = 8.dp, end = 16.dp)
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .border(1.dp, Color.Black.copy(alpha = 0.6f), CircleShape)
                                    .padding(1.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.profile),
                                contentDescription = "Foto de perfil por defecto",
                                modifier = Modifier
                                    .padding(top = 8.dp, end = 16.dp)
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Black.copy(alpha = 0.6f), CircleShape)
                                    .padding(1.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF3B82F6)),
                modifier = Modifier
                    .height(56.dp)
                    .clip(CustomShape())
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(BottomBarCustomShape())
                        .background(Color(0xFF3B82F6))
                ) {
                    BottomNavigationBar(
                        navController = navController,
                        onLogoutClick = { showDialog = DialogType.Logout }
                    )
                }
                Box(
                    modifier = Modifier
                        .offset(y = (-25).dp)
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2196F3))
                        .padding(16.dp)
                        .clickable { navigateToPrincipal() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Inicio",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route
            ) {
                composable(BottomNavItem.Home.route) {
                    when (seccion) {
                        "Populares" -> {
                            if (progressBar_populares) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4),
                                    modifier = Modifier
                                        .padding(bottom = 15.dp)
                                ) {
                                    items(lista_populares!!) { pelicula ->
                                        PeliculaItem(pelicula, navigateToDetail)
                                    }
                                }
                            }
                        }

                        "Rated" -> {
                            if (progressBar_rated) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            } else {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4),
                                    modifier = Modifier
                                        .padding( bottom = 15.dp)
                                ) {
                                    items(lista_rated!!) { pelicula ->
                                        PeliculaItem(pelicula, navigateToDetail)
                                    }
                                }
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
                composable(BottomNavItem.Search.route) {
                    /*SearchContent()*/
                }
                composable(BottomNavItem.Watch.route) {
                    /*WatchlistContent()*/
                }
                composable(BottomNavItem.Profile.route) {
                    PerfilScreen(auth, favoritasViewModel)
                }
            }
        }
    }
}

@Composable
private fun PeliculaItem(pelicula: MediaItem, navigateToDetail: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .width(140.dp)
            .height(180.dp)
            .clickable { navigateToDetail(pelicula.id) }
            .border(
                width = 0.5.dp,
                color = Color.Gray.copy(alpha = 0.7f)
            )
    ) {
        Imagen(item = pelicula)
    }
}

@Composable
fun Imagen(item: MediaItem, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val painter = rememberAsyncImagePainter(
        model = item.poster,
        imageLoader = ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    )

    val painterState = painter.state

    Box(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (painterState is AsyncImagePainter.State.Error || item.poster.isNullOrBlank()) {
            Text(
                text = "Poster no disponible",
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(4.dp))
                    .padding(8.dp)
            )
        }
    }
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

