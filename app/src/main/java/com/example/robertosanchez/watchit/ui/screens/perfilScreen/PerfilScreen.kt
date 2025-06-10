package com.example.robertosanchez.watchit.ui.screens.perfilScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.robertosanchez.watchit.data.AuthManager
import com.example.robertosanchez.watchit.R
import com.example.robertosanchez.watchit.db.FirestoreManager
import com.example.robertosanchez.watchit.db.Pelicula.Pelicula
import com.example.robertosanchez.watchit.ui.shapes.CustomShape
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.robertosanchez.watchit.ui.screens.peliculasVistasScreen.PeliculasVistasViewModel
import com.example.robertosanchez.watchit.ui.screens.peliculasVistasScreen.PeliculasVistasViewModelFactory
import com.example.robertosanchez.watchit.ui.screens.reviewsUsuarioScreen.ReviewsUsuarioViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    auth: AuthManager,
    firestore: FirestoreManager,
    navigateToDetail: (Int) -> Unit,
    navigateToVistas: () -> Unit,
    navigateToReviews: () -> Unit
) {
    val user = auth.getCurrentUser()
    var peliculasFavoritas by remember { mutableStateOf<List<Pelicula>>(emptyList()) }
    val peliculasVistasViewModel: PeliculasVistasViewModel = viewModel(
        factory = PeliculasVistasViewModelFactory(firestore, auth)
    )
    val peliculasVistas by peliculasVistasViewModel.uiState.collectAsState()

    LaunchedEffect(user) {
        if (user != null) {
            firestore.getFavoriteMovies(user.uid).collect { peliculas ->
                peliculasFavoritas = peliculas
            }
            peliculasVistasViewModel.loadVistas()
        } else {
            peliculasFavoritas = emptyList()
        }
    }

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
                            text = "Perfil",
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
                .padding(top = 120.dp)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color(0xFF3B82F6), CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (user?.photoUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(user.photoUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.profile),
                        contentDescription = "Foto de perfil por defecto",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (user?.isAnonymous == true) "Anónimo" else (user?.displayName ?: "Usuario"),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (user?.isAnonymous == true) "correo anónimo" else (user?.email ?: "Email no disponible"),
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (user?.isAnonymous == true) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Debes iniciar sesión para ver/guardar tus películas favoritas.",
                        fontSize = 18.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(4) { index ->
                        if (index < peliculasFavoritas.size) {
                            val pelicula = peliculasFavoritas[index]
                            Box(
                                modifier = Modifier
                                    .size(width = 80.dp, height = 120.dp)
                                    .clickable { navigateToDetail(pelicula.peliculaId) }
                                    .border(
                                        width = 1.dp,
                                        color = Color.Gray.copy(alpha = 0.7f)
                                    )
                            ) {
                                pelicula.poster?.let { posterUrl ->
                                    AsyncImage(
                                        model = "https://image.tmdb.org/t/p/w185$posterUrl",
                                        contentDescription = "Poster de película",
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(width = 80.dp, height = 120.dp)
                                    .background(Color.Gray.copy(alpha = 0.3f))
                                    .border(
                                        width = 1.dp,
                                        color = Color.Gray.copy(alpha = 0.7f)
                                    )
                            ) {
                                // Vacio
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Texto a la izquierda
                Text(
                    text = "Películas Vistas",
                    fontSize = 16.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (user?.isAnonymous == true) {
                        Text(
                            text = "Inicia sesión para ver/guardar tus películas",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(end = 8.dp)
                        )
                    } else if (peliculasVistas.peliculas.isEmpty()) {
                        Text(
                            text = "0",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .clickable { navigateToVistas() }
                                .padding(end = 8.dp)
                        )
                    } else {
                        Text(
                            text = peliculasVistas.peliculas.size.toString(),
                            fontSize = 14.sp,
                            modifier = Modifier
                                .clickable { navigateToVistas() }
                                .padding(end = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Texto a la izquierda
                Text(
                    text = "Reviews",
                    fontSize = 16.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (user?.isAnonymous == true) {
                        Text(
                            text = "Inicia sesión para ver tus reviews",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(end = 8.dp)
                        )
                    } else {
                        val reviewsUsuarioViewModel: ReviewsUsuarioViewModel = remember {
                            ReviewsUsuarioViewModel(firestore, auth)
                        }
                        val reviewsState by reviewsUsuarioViewModel.uiState.collectAsState()
                        
                        Text(
                            text = reviewsState.reviews.size.toString(),
                            fontSize = 14.sp,
                            modifier = Modifier
                                .clickable { navigateToReviews() }
                                .padding(end = 8.dp)
                        )
                    }
                }
            }
        }
    }
}