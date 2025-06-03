package com.example.robertosanchez.watchit.ui.screens.principalScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.robertosanchez.watchit.data.AuthManager
import com.example.robertosanchez.watchit.R
import com.example.robertosanchez.watchit.data.model.MediaItem
import com.example.robertosanchez.watchit.db.FirestoreManager
import com.example.robertosanchez.watchit.ui.navegacion.BottomNavigationBar
import com.example.robertosanchez.watchit.ui.navegacion.BottomNavItem
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.BusquedaScreen
import com.example.robertosanchez.watchit.ui.screens.perfilScreen.PerfilScreen
import com.example.robertosanchez.watchit.ui.screens.watchListScreen.WatchListScreen
import com.example.robertosanchez.watchit.ui.shapes.BottomBarCustomShape
import com.example.robertosanchez.watchit.ui.shapes.CustomShape

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalScreen(popularesViewModel: PeliculasPopularesViewModel,
                    ratedViewModel: PeliculasRatedViewModel,
                    firestore: FirestoreManager,
                    auth: AuthManager,
                    navigateToLogin: () -> Unit,
                    navigateToDetail: (Int) -> Unit,
                    navigateToListaLarga: (SeccionType) -> Unit,
                    navigateToBusquedaNombre: (String) -> Unit,
                    navigateToListaFecha: () -> Unit,
                    navigateToListaGenero: () -> Unit,
                    navigateToProximosEstrenos: () -> Unit,
                    navigateToEnCine: () -> Unit,
                    navigateToVistas: () -> Unit
) {
    var showDialog by remember { mutableStateOf<DialogType?>(null) }
    val navController = rememberNavController()

    val lista_populares by popularesViewModel.lista.observeAsState(emptyList())
    val progressBar_populares by popularesViewModel.progressBar.observeAsState(false)

    val lista_rated by ratedViewModel.lista.observeAsState(emptyList())
    val progressBar_rated by ratedViewModel.progressBar.observeAsState(false)

    Scaffold(
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
                // Botón flotante de inicio
                Box(
                    modifier = Modifier
                        .offset(y = (-25).dp)
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2196F3))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = { navController.navigate(BottomNavItem.Home.route) }
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
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route
            ) {
                composable(BottomNavItem.Home.route) {
                    SeccionPeliculas(
                        lista_populares = lista_populares,
                        progressBar_populares = progressBar_populares,
                        lista_rated = lista_rated,
                        progressBar_rated = progressBar_rated,
                        auth = auth,
                        navigateToDetail = navigateToDetail,
                        navigateToListaLarga = navigateToListaLarga
                    )
                }
                composable(BottomNavItem.Search.route) {
                    BusquedaScreen(
                        auth = auth,
                        navigateToBusquedaNombre = navigateToBusquedaNombre,
                        navigateToListaFecha = navigateToListaFecha,
                        navigateToListaGenero = navigateToListaGenero,
                        navigateToProximosEstrenos = navigateToProximosEstrenos,
                        navigateToEnCines = navigateToEnCine
                    )
                }
                composable(BottomNavItem.Watch.route) {
                    WatchListScreen(
                        auth = auth,
                        firestore = firestore,
                        navigateToDetail = navigateToDetail
                    )
                }
                composable(BottomNavItem.Profile.route) {
                    PerfilScreen(auth, firestore, navigateToDetail, navigateToVistas)
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
private fun PeliculasListItem(pelicula: MediaItem, navigateToDetail: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .width(140.dp)
            .height(200.dp)
            .clickable { navigateToDetail(pelicula.id) }
            .border(
                width = 2.dp,
                color = Color.Gray.copy(alpha = 0.7f)
            )
    ) {
        Imagen(item = pelicula)
    }
}

@Composable
fun Imagen(item: MediaItem, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Image(
        painter = rememberAsyncImagePainter(
            model = item.poster,
            imageLoader = ImageLoader.Builder(context).crossfade(true).build()
        ),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        contentScale = ContentScale.Crop
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeccionPeliculas(
    lista_populares: List<MediaItem>?,
    progressBar_populares: Boolean,
    lista_rated: List<MediaItem>?,
    progressBar_rated: Boolean,
    auth: AuthManager,
    navigateToDetail: (Int) -> Unit,
    navigateToListaLarga: (SeccionType) -> Unit,
) {
    val user = auth.getCurrentUser()

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Inicio",
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
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 120.dp, bottom = 26.dp)
        ) {
            // Peliculas Populares
            Text(
                text = "Peliculas Populares esta Semana",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            if (progressBar_populares) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (lista_populares!!.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay elementos", style = MaterialTheme.typography.bodySmall)
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(lista_populares!!.take(10)) { pelicula ->
                            PeliculasListItem(pelicula, navigateToDetail)
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(200.dp)
                                    .background(Color.Gray.copy(alpha = 0.3f))
                                    .border(
                                        width = 2.dp,
                                        color = Color.Gray.copy(alpha = 0.7f)
                                    )
                                    .clickable { navigateToListaLarga(SeccionType.Populares) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Ver Más",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Peliculas mejor Valoradas
            Text(
                text = "Peliculas mejor Valoradas",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            if (progressBar_rated) {
                Box(
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (lista_rated!!.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay elementos", style = MaterialTheme.typography.bodySmall)
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(lista_rated!!.take(10)) { pelicula ->
                            PeliculasListItem(pelicula, navigateToDetail)
                        }
                        item {
                            Box(
                                modifier = Modifier
                                    .width(140.dp)
                                    .height(200.dp)
                                    .background(Color.Gray.copy(alpha = 0.3f))
                                    .border(
                                        width = 2.dp,
                                        color = Color.Gray.copy(alpha = 0.7f)
                                    )
                                    .clickable { navigateToListaLarga(SeccionType.Rated) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Ver Más",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Enum para los tipos de diálogos
enum class DialogType {
    Logout
}

// Enum para los tipos de secciones
enum class SeccionType {
    Populares, Rated
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