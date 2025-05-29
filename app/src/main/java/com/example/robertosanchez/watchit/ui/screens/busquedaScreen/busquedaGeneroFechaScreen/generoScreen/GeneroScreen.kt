package com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaGeneroFechaScreen.generoScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.robertosanchez.watchit.R
import com.example.robertosanchez.watchit.data.AuthManager
import com.example.robertosanchez.watchit.data.model.MediaItem
import com.example.robertosanchez.watchit.ui.shapes.CustomShape

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneroScreen(
    viewModel: GeneroViewModel,
    auth: AuthManager,
    navigateToDetail: (Int) -> Unit,
    navigateBack: () -> Unit,
) {
    val user = auth.getCurrentUser()

    val lista_buscada by viewModel.lista.observeAsState(emptyList())
    val progressBar_buscada by viewModel.progressBar.observeAsState(false)

    val generoBuscado = viewModel.generoSeleccionado
    val nombreGenero = when(generoBuscado) {
        28 -> "Acción"
        12 -> "Aventura"
        16 -> "Animación"
        35 -> "Comedia"
        80 -> "Crimen"
        99 -> "Documental"
        18 -> "Drama"
        10751 -> "Familia"
        14 -> "Fantasía"
        36 -> "Historia"
        27 -> "Terror"
        10402 -> "Música"
        9648 -> "Misterio"
        10749 -> "Romance"
        878 -> "Ciencia ficción"
        10770 -> "TV Movie"
        53 -> "Thriller"
        10752 -> "Bélica"
        37 -> "Western"
        else -> "Películas"
    }

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
                                .align(Alignment.Top)
                                .zIndex(1f)
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Atrás",
                                tint = Color.Black
                            )
                        }

                        Text(
                            text = "Películas de $nombreGenero",
                            fontSize = 18.sp,
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
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (progressBar_buscada) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF3B82F6),
                        modifier = Modifier.size(48.dp)
                    )
                }
            } else if (lista_buscada.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Sin resultados",
                            tint = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No se encontraron películas",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4)
                ) {
                    items(lista_buscada!!) { pelicula ->
                        PeliculaItem(pelicula, navigateToDetail)
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
