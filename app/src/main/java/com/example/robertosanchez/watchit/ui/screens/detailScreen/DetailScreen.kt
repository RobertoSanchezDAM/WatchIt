package com.example.robertosanchez.watchit.ui.screens.detailScreen

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
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
import com.example.robertosanchez.watchit.db.PeliculasVistas.PeliculasVistas
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaEnCines.EnCineViewModel
import com.example.robertosanchez.watchit.ui.screens.busquedaScreen.busquedaProximosEstrenos.ProximosEstrenosViewModel
import com.example.robertosanchez.watchit.ui.screens.peliculasVistasScreen.PeliculasVistasViewModel
import com.example.robertosanchez.watchit.ui.screens.watchListScreen.WatchListViewModel
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.viewinterop.AndroidView
import com.example.robertosanchez.watchit.repositories.models.CastMember
import com.example.robertosanchez.watchit.repositories.models.WatchProviders
import com.example.robertosanchez.watchit.repositories.models.MovieVideosResponse
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.PlayArrow
import com.example.robertosanchez.watchit.repositories.models.Review


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
    peliculasVistasViewModel: PeliculasVistasViewModel,
    navigateBack: () -> Unit,
    auth: AuthManager,
) {
    val user = auth.getCurrentUser()
    val scope = rememberCoroutineScope()
    val reviewsViewModel: DetailReviewsViewModel = viewModel()
    val reviews by reviewsViewModel.reviews.observeAsState(emptyList())

    var reviewText by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    val isFavorita = peliculasFavoritasViewModel.isFavorita(id)
    var localFavoritaState by remember { mutableStateOf(isFavorita) }

    val isAddWatchList = watchListViewModel.isWatchList(id)
    var localWatchListState by remember { mutableStateOf(isAddWatchList) }

    val isVista = peliculasVistasViewModel.isVista(id)
    var localVistaState by remember { mutableStateOf(isVista) }

    val context = LocalContext.current

    LaunchedEffect(user) {
        peliculasFavoritasViewModel.loadFavorites()
        watchListViewModel.loadWatchList()
        peliculasVistasViewModel.loadVistas()
        reviewsViewModel.loadReviews(id)
    }

    LaunchedEffect(user, isFavorita, isAddWatchList, isVista) {
        localFavoritaState = isFavorita
        localWatchListState = isAddWatchList
        localVistaState = isVista
    }

    val listaPopulares by popularesViewModel.lista.observeAsState(emptyList())
    val listaRated by ratedViewModel.lista.observeAsState(emptyList())
    val listaEnCine by enCineViewModel.lista.observeAsState(emptyList())
    val listaProximosEstrenos by proximosEstrenosViewModel.lista.observeAsState(emptyList())

    val detailGeneroViewModel: DetailGeneroViewModel = viewModel()
    val listaGenero by detailGeneroViewModel.lista.observeAsState(emptyList())

    val creditosViewModel: DetailCreditosViewModel = viewModel()
    val creditos by creditosViewModel.creditos.observeAsState()

    val plataformasViewModel: DetailPlataformasViewModel = viewModel()
    val credits by plataformasViewModel.plataformas.observeAsState()

    val videosViewModel: DetailVideosViewModel = viewModel()
    val videos by videosViewModel.videos.observeAsState()

    val imagenesViewModel: DetailImagenesViewModel = viewModel()
    val imagenes by imagenesViewModel.imagenes.observeAsState()

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
        creditosViewModel.fetchCreditos(id)
        imagenesViewModel.fetchImagenes(id)
        plataformasViewModel.fetchPlataformas(id)
        videosViewModel.fetchVideos(id)
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

    val generosId = pelicula?.generos_ids ?: emptyList()
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
                .fillMaxSize()
                .background(Color(0xFF1E1E1E))
        ) {
            item {
                if (pelicula != null) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        val backdropPath = imagenes?.backdrops?.maxByOrNull { it.width ?: 0 }?.file_path
                            ?: imagenes?.posters?.maxByOrNull { it.width ?: 0 }?.file_path
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
                                        .width(120.dp)
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text(
                                        pelicula.title,
                                        color = Color.White,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    creditos?.let { cr ->
                                        val director = cr.crew.find { it.job == "Director" }
                                        director?.let {
                                            Text(
                                                "Dirigido por ${it.name}",
                                                color = Color.LightGray,
                                                fontSize = 16.sp
                                            )
                                        }
                                    }

                                    Text(pelicula.release_date, color = Color(0xFF838383), fontSize = 14.sp)

                                    if (generosNombre.isNotEmpty()) {
                                        Text(
                                            text = generosNombre.joinToString(", "),
                                            color = Color(0xFF838383),
                                            fontSize = 12.sp,
                                        )
                                    }

                                    VideosSection(videos)
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
                                        if (localFavoritaState) {
                                            localFavoritaState = false
                                            peliculasFavoritasViewModel.removePeliculaFavorita(
                                                Pelicula(
                                                    peliculaId = pelicula.id,
                                                    poster = pelicula.poster
                                                )
                                            )
                                            Toast.makeText(context, "Película eliminada de favoritos", Toast.LENGTH_SHORT).show()
                                        } else {
                                            if (peliculasFavoritasViewModel.puedoAnadirMasFavoritas()) {
                                                localFavoritaState = true
                                                val success = peliculasFavoritasViewModel.addPeliculaFavorita(
                                                    Pelicula(
                                                        peliculaId = pelicula.id,
                                                        poster = pelicula.poster
                                                    )
                                                )
                                                if (success) {
                                                    Toast.makeText(context, "Película añadida a favoritos", Toast.LENGTH_SHORT).show()
                                                } else {
                                                    localFavoritaState = false
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
                                        imageVector = if (localFavoritaState) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                        contentDescription = if (localFavoritaState) "Eliminar de favoritos" else "Añadir a favoritos",
                                        tint = if (user == null) Color.Gray else if (localFavoritaState) Color.Red else Color.Gray
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

                                IconButton(onClick = {
                                    if (user?.isAnonymous == true) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = "Debes iniciar sesión para añadir a tu lista",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    } else if (pelicula != null) {
                                        if (localVistaState) {
                                            localVistaState = false
                                            peliculasVistasViewModel.removeVista(
                                                PeliculasVistas(
                                                    peliculaId = pelicula.id,
                                                    poster = pelicula.poster
                                                )
                                            )
                                            Toast.makeText(context, "Película vista eliminada", Toast.LENGTH_SHORT).show()
                                        } else {
                                            val success = peliculasVistasViewModel.addVista(
                                                PeliculasVistas(
                                                    peliculaId = pelicula.id,
                                                    poster = pelicula.poster
                                                )
                                            )
                                            if (success) {
                                                Toast.makeText(context, "Película vista añadida", Toast.LENGTH_SHORT).show()
                                                localVistaState = true
                                            }
                                        }
                                    }
                                }) {
                                    Icon(
                                        painter = painterResource(R.drawable.eye),
                                        contentDescription = if (localVistaState) "Marcada como vista" else "Marcar como vista",
                                        tint = if (user == null) Color.Gray else if (localVistaState) Color.Cyan else Color.Gray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            if (localVistaState) {
                                var localValoracion by remember { mutableStateOf(peliculasVistasViewModel.getRating(id)) }

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                color = Color(0xFF2A2A2A),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Estrellas(
                                                valoracion = localValoracion,
                                                onValoracionSelected = { valoracion ->
                                                    localValoracion = valoracion
                                                    scope.launch {
                                                        if (user != null) {
                                                            peliculasVistasViewModel.updateRating(id, valoracion)
                                                            snackbarHostState.showSnackbar("Valoración guardada")
                                                        }
                                                    }
                                                },
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                            Text(
                                                text = "Valorar",
                                                color = Color.LightGray,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(top = 2.dp),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

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

                            Divider(
                                color = Color.Gray.copy(alpha = 0.3f),
                                thickness = 1.dp,
                                modifier = Modifier.fillMaxWidth()
                            )

                            creditos?.cast?.let { castList ->
                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Reparto principal",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    CastSeccion(castList)
                                }

                               Spacer(modifier = Modifier.height(16.dp))


                                Divider(
                                    color = Color.Gray.copy(alpha = 0.3f),
                                    thickness = 1.dp,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    PlataformasSeccion(credits)
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

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(
                    color = Color.Gray.copy(alpha = 0.3f),
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Sección de Reviews
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Reviews",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Campo para escribir review
                    if (user != null && !user.isAnonymous) {
                        OutlinedTextField(
                            value = reviewText,
                            onValueChange = { reviewText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            placeholder = { Text("Escribe tu review...", color = Color.Gray) },
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White,
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.Gray
                            ),
                            maxLines = 3
                        )

                        Button(
                            onClick = {
                                if (reviewText.isNotBlank()) {
                                    val review = Review(
                                        movieId = id,
                                        userId = user.uid,
                                        userName = user.displayName ?: "Usuario",
                                        userPhotoUrl = user.photoUrl?.toString(),
                                        text = reviewText
                                    )
                                    reviewsViewModel.addReview(
                                        review = review,
                                        onSuccess = {
                                            reviewText = ""
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Review añadida con éxito")
                                            }
                                        },
                                        onError = { error ->
                                            scope.launch {
                                                snackbarHostState.showSnackbar(error)
                                            }
                                        }
                                    )
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(bottom = 16.dp),
                            enabled = reviewText.isNotBlank()
                        ) {
                            Text("Enviar Review")
                        }
                    } else {
                        Text(
                            text = if (user?.isAnonymous == true) 
                                "Inicia sesión para escribir una review" 
                            else 
                                "Inicia sesión para escribir una review",
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // Lista de reviews
                    if (reviews.isEmpty()) {
                        Text(
                            text = "No hay reviews todavía. ¡Sé el primero en escribir una!",
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    } else {
                        reviews.forEach { review ->
                            ReviewItem(review = review)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Estrellas(
    valoracion: Int,
    onValoracionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) { index ->
            IconButton(
                onClick = { 
                    val nuevaValoracion = if (index + 1 == valoracion) 0 else (index + 1)
                    onValoracionSelected(nuevaValoracion)
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Estrella ${index + 1}",
                    tint = if (index < valoracion) Color(0xFFFFD700) else Color.Gray,
                    modifier = Modifier
                        .size(32.dp)
                        .alpha(if (index < valoracion) 1f else 0.3f)
                )
            }
        }
    }
}

@Composable
fun CastSeccion(castList: List<CastMember>) {
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
        }
    }
}

@Composable
fun PlataformasSeccion(plataformas: WatchProviders?) {
    val esProviders = plataformas?.results?.get("ES")
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Donde Ver",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        if (esProviders != null) {
            // Streaming
            if (!esProviders.flatrate.isNullOrEmpty()) {
                Text(
                    text = "Streaming",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    esProviders.flatrate.forEach { provider ->
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/original${provider.logo_path}",
                            contentDescription = provider.provider_name,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
            
            // Alquiler
            if (!esProviders.rent.isNullOrEmpty()) {
                Text(
                    text = "Alquiler",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    esProviders.rent.forEach { provider ->
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/original${provider.logo_path}",
                            contentDescription = provider.provider_name,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
            
            // Compra
            if (!esProviders.buy.isNullOrEmpty()) {
                Text(
                    text = "Compra",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    esProviders.buy.forEach { provider ->
                        AsyncImage(
                            model = "https://image.tmdb.org/t/p/original${provider.logo_path}",
                            contentDescription = provider.provider_name,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(8.dp))
                        )
                    }
                }
            }
        } else {
            Text(
                text = "No disponible en streaming en España",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun VideosSection(videos: MovieVideosResponse?) {
    val trailer = videos?.results?.find { it.type == "Trailer" && it.site == "YouTube" }
    val context = LocalContext.current
    
    if (trailer != null) {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=${trailer.key}"))
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .width(145.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B82F6)
                )
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Play",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ver Tráiler",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2A2A2A)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Foto de perfil
                if (review.userPhotoUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(review.userPhotoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = review.userName.first().toString(),
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = review.userName,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
                            .format(java.util.Date(review.timestamp)),
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = review.text,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}