package com.example.robertosanchez.watchit.ui.screens.detailScreen

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.robertosanchez.watchit.ui.screens.principalScreen.PeliculasPopularesViewModel
import com.example.robertosanchez.watchit.ui.screens.principalScreen.PeliculasRatedViewModel
import com.example.robertosanchez.watchit.ui.shapes.CustomShape
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.robertosanchez.watchit.db.Pelicula.Pelicula
import com.example.robertosanchez.watchit.ui.screens.perfilScreen.PeliculasFavoritasViewModel
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.robertosanchez.watchit.R
import com.example.robertosanchez.watchit.data.AuthManager
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaEnCines.EnCineViewModel
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaProximosEstrenos.ProximosEstrenosViewModel
import com.example.robertosanchez.watchit.ui.screens.watchListScreen.WatchListViewModel
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: Int,
    popularesViewModel: PeliculasPopularesViewModel,
    ratedViewModel: PeliculasRatedViewModel,
    peliculasFavoritasViewModel: PeliculasFavoritasViewModel,
    enCineViewModel: EnCineViewModel,
    proximosEstrenosViewModel: ProximosEstrenosViewModel,
    watchListViewModel: WatchListViewModel,
    navigateBack: () -> Unit,
    auth: AuthManager,
) {
    val user = auth.getCurrentUser()
    val scope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }
    val isFavorite = peliculasFavoritasViewModel.isFavorite(id)
    var localFavoriteState by remember { mutableStateOf(isFavorite) }

    val isAddWatchList = watchListViewModel.isWatched(id)
    var localWatchListState by remember { mutableStateOf(isAddWatchList) }

    val context = LocalContext.current

    LaunchedEffect(user) {
        peliculasFavoritasViewModel.loadFavorites()
        watchListViewModel.loadWatchList()
    }

    LaunchedEffect(user, isFavorite, isAddWatchList) {
        localFavoriteState = isFavorite
        localWatchListState = isAddWatchList
    }

    val listaPopulares by popularesViewModel.lista.observeAsState(emptyList())
    val listaRated by ratedViewModel.lista.observeAsState(emptyList())
    val listaEnCine by enCineViewModel.lista.observeAsState(emptyList())
    val listaProximosEstrenos by proximosEstrenosViewModel.lista.observeAsState(emptyList())

    val detailGeneroViewModel: DetailGeneroViewModel = viewModel()
    val listaGenero by detailGeneroViewModel.lista.observeAsState(emptyList())

    val creditsViewModel: DetailCreditsViewModel = viewModel()
    val credits by creditsViewModel.credits.observeAsState()

    val imagesViewModel: DetailImagesViewModel = viewModel()
    val images by imagesViewModel.images.observeAsState()

    val generos = mapOf(
        28 to "Acción",
        12 to "Aventura",
        16 to "Animación",
        35 to "Comedia",
        80 to "Crimen",
        99 to "Documental",
        18 to "Drama",
        10751 to "Familia",
        14 to "Fantasía",
        36 to "Historia",
        27 to "Terror",
        10402 to "Música",
        9648 to "Misterio",
        10749 to "Romance",
        878 to "Ciencia ficción",
        10770 to "TV Movie",
        53 to "Thriller",
        10752 to "Bélica",
        37 to "Western"
    )

    LaunchedEffect(id) {
        creditsViewModel.fetchCredits(id)
        imagesViewModel.fetchImages(id)
    }

    // Efecto separado para buscar la película por ID si no está en las listas
    LaunchedEffect(listaPopulares, listaRated, listaEnCine, listaProximosEstrenos) {
        if (listaPopulares.find { it.id == id } == null &&
            listaRated.find { it.id == id } == null &&
            listaEnCine.find { it.id == id } == null &&
            listaProximosEstrenos.find { it.id == id } == null &&
            listaGenero.find { it.id == id } == null) {
            detailGeneroViewModel.buscarPeliculaPorId(id)
        }
    }

    val pelicula = remember(id, listaPopulares, listaRated, listaEnCine, listaProximosEstrenos, listaGenero) {
        listaPopulares.find { it.id == id } ?:
        listaRated.find { it.id == id } ?:
        listaEnCine.find { it.id == id } ?:
        listaProximosEstrenos.find { it.id == id } ?:
        listaGenero.find { it.id == id }
    }

    val generosId = pelicula?.genre_ids ?: emptyList()
    val generosNombre = generosId.mapNotNull { generos[it] }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
                            text = "Detalles de la Película",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF3B82F6),
                    titleContentColor = Color.White
                ),
                modifier = Modifier
                    .height(56.dp)
                    .clip(CustomShape())
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            item {
                if (pelicula != null) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        val backdropPath = images?.backdrops?.maxByOrNull { it.width ?: 0 }?.file_path
                            ?: images?.posters?.maxByOrNull { it.width ?: 0 }?.file_path
                            ?: pelicula.poster
                        Image(
                            painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/original$backdropPath"),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(350.dp),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(340.dp)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.7f),
                                            Color.Transparent,
                                            Color(0xFF1E1E1E)
                                        ),
                                        startY = 0f,
                                        endY = 625f
                                    )
                                )
                        )
                        // Contenido principal
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 220.dp)
                                .background(Color(0xFF1E1E1E))
                                .padding(16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = rememberAsyncImagePainter("https://image.tmdb.org/t/p/original${pelicula.poster}"),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(110.dp)
                                        .height(150.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        pelicula.title,
                                        color = Color.White,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    credits?.let { cr ->
                                        val director = cr.crew.find { it.job == "Director" }
                                        director?.let {
                                            Text(
                                                "Dirigido por ${it.name}",
                                                color = Color.LightGray,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Text(pelicula.release_date, color = Color.LightGray, fontSize = 14.sp)

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (generosNombre.isNotEmpty()) {
                                        Text(
                                            text = generosNombre.joinToString(", "),
                                            color = Color.LightGray,
                                            fontSize = 12.sp,
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row (
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    if (user?.isAnonymous == true) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "Debes iniciar sesión para añadir a favoritos",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    } else if (pelicula != null) {
                                        if (localFavoriteState) {
                                            localFavoriteState = false
                                            peliculasFavoritasViewModel.removeFavoriteMovie(
                                                Pelicula(
                                                    peliculaId = pelicula.id,
                                                    poster = pelicula.poster
                                                )
                                            )
                                            Toast.makeText(context, "Película eliminada de favoritos", Toast.LENGTH_SHORT).show()
                                        } else {
                                            if (peliculasFavoritasViewModel.canAddMoreFavorites()) {
                                                localFavoriteState = true
                                                val success = peliculasFavoritasViewModel.addFavoriteMovie(
                                                    Pelicula(
                                                        peliculaId = pelicula.id,
                                                        poster = pelicula.poster
                                                    )
                                                )
                                                if (success) {
                                                    Toast.makeText(context, "Película añadida a favoritos", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    localFavoriteState = false
                                                    scope.launch {
                                                        snackbarHostState.showSnackbar(
                                                            message = "No se pudo añadir la película a favoritos",
                                                            duration = SnackbarDuration.Short
                                                        )
                                                    }
                                                }
                                            } else {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar(
                                                        message = "No puedes añadir más de 4 películas favoritas",
                                                        duration = SnackbarDuration.Short
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (localFavoriteState) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                        contentDescription = if (localFavoriteState) "Eliminar de favoritos" else "Añadir a favoritos",
                                        tint = if (user == null) Color.Gray else if (localFavoriteState) Color.Red else Color.Gray
                                    )
                                }

                                IconButton(onClick = {
                                    if (user?.isAnonymous == true) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "Debes iniciar sesión para añadir a tu lista",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    } else if (pelicula != null) {
                                        if (localWatchListState) {
                                            localWatchListState = false
                                            watchListViewModel.removeWatchList(
                                                Pelicula(
                                                    peliculaId = pelicula.id,
                                                    poster = pelicula.poster
                                                )
                                            )
                                            Toast.makeText(context, "Película para ver eliminada", Toast.LENGTH_SHORT).show()
                                        } else {
                                            val success = watchListViewModel.addWatchList(
                                                Pelicula(
                                                    peliculaId = pelicula.id,
                                                    poster = pelicula.poster
                                                )
                                            )
                                            if (success) {
                                                Toast.makeText(context, "Película para ver añadida", Toast.LENGTH_SHORT).show()
                                                localWatchListState = true
                                            }
                                        }
                                    }
                                }) {
                                    Icon(
                                        imageVector = if (localWatchListState) Icons.Filled.Star else Icons.Outlined.Star,
                                        contentDescription = if (localWatchListState) "Eliminar de la lista para Ver" else "Añadir a la lista para Ver",
                                        tint = if (user == null) Color.Gray else if (localWatchListState) Color.Yellow else Color.Gray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            var expandir by remember { mutableStateOf(false) }
                            Text(
                                text = "Sinopsis",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            val sinopsis = pelicula.sinopsis ?: "Sinopsis no disponible."
                            val maxLineas = if (expandir) Int.MAX_VALUE else 3
                            Text(
                                text = sinopsis,
                                color = Color.White,
                                maxLines = maxLineas,
                                fontSize = 16.sp
                            )
                            if (!expandir && sinopsis.length > 120) {
                                TextButton(
                                    onClick = { expandir = true },
                                    modifier = Modifier.padding(0.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Leer más", color = Color.Gray)
                                }
                            }
                            if (expandir && sinopsis.length > 120) {
                                TextButton(
                                    onClick = { expandir = false },
                                    modifier = Modifier.padding(0.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("Leer menos", color = Color.Gray)
                                }
                            }

                            credits?.cast?.take(10)?.let { castList ->
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Reparto principal",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    castList.forEach { actor ->
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            modifier = Modifier.width(60.dp)
                                        ) {
                                            val profileUrl = actor.profile_path?.let { "https://image.tmdb.org/t/p/w185$it" }
                                            if (profileUrl != null) {
                                                Image(
                                                    painter = rememberAsyncImagePainter(profileUrl),
                                                    contentDescription = actor.name,
                                                    modifier = Modifier
                                                        .size(48.dp)
                                                        .clip(CircleShape),
                                                    contentScale = ContentScale.Crop
                                                )
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .size(48.dp)
                                                        .clip(CircleShape)
                                                        .background(Color.Gray),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = actor.name,
                                                        color = Color.White,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                            Text(
                                                text = actor.name,
                                                color = Color.White,
                                                fontSize = 12.sp,
                                                modifier = Modifier.fillMaxWidth(),
                                                maxLines = 2,
                                                textAlign = TextAlign.Center,
                                                lineHeight = 1.5.em
                                            )
                                            Spacer(modifier = Modifier.height(100.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Película no encontrada",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}