package com.example.robertosanchez.watchinit.ui.screens.listaLargaPeliculas

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.robertosanchez.watchinit.R
import com.example.robertosanchez.watchinit.data.AuthManager
import com.example.robertosanchez.watchinit.data.model.MediaItem
import com.example.robertosanchez.watchinit.ui.screens.principalScreen.PeliculasPopularesViewModel
import com.example.robertosanchez.watchinit.ui.screens.principalScreen.PeliculasRatedViewModel
import com.example.robertosanchez.watchinit.ui.shapes.CustomShape

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListaLargaPeliculasScreen(
    popularesViewModel: PeliculasPopularesViewModel,
    ratedViewModel: PeliculasRatedViewModel,
    seccion: String,
    navigateToDetail: (Int) -> Unit,
    auth: AuthManager,
    navigateBack: () -> Unit,
    navigateToPrincipal: () -> Unit
) {
    val user = auth.getCurrentUser()

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
                        IconButton(
                            onClick = navigateBack,
                            modifier = Modifier
                                .zIndex(1f)
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Atrás",
                                tint = Color.Black
                            )
                        }

                        Text(
                            text = when (seccion) {
                                "Populares" -> "Películas Populares esta Semana"
                                "Rated" -> "Películas mejor Valoradas"
                                else -> "Lista de Películas"
                            },
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black.copy(alpha = 0.8f)
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToPrincipal,
                containerColor = Color(0xFF3B82F6),
                contentColor = Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = "Ir a inicio",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
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
                            columns = GridCells.Fixed(4)
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

