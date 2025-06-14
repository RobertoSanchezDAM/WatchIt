package com.example.robertosanchez.watchinit.ui.screens.perfilUsuarioSelectScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.robertosanchez.watchinit.R
import com.example.robertosanchez.watchinit.data.AuthManager
import com.example.robertosanchez.watchinit.db.FirestoreManager
import com.example.robertosanchez.watchinit.db.Pelicula.Pelicula
import com.example.robertosanchez.watchinit.ui.shapes.CustomShape
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.robertosanchez.watchinit.ui.screens.peliculasVistasScreen.PeliculasVistasViewModel
import com.example.robertosanchez.watchinit.ui.screens.peliculasVistasScreen.PeliculasVistasViewModelFactory
import com.example.robertosanchez.watchinit.ui.screens.reviewsUsuarioScreen.ReviewsUsuarioViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilUsuarioSelectScreen(
    userId: String,
    userName: String,
    userPhotoUrl: String?,
    auth: AuthManager,
    firestore: FirestoreManager,
    navigateToDetail: (Int) -> Unit,
    navigateBack: () -> Unit,
    navigateToVistas: () -> Unit,
    navigateToReviews: () -> Unit,
    navigateToPrincipal: () -> Unit
) {
    val user = auth.getCurrentUser()

    var peliculasFavoritas by remember { mutableStateOf<List<Pelicula>>(emptyList()) }
    val peliculasVistasViewModel: PeliculasVistasViewModel = viewModel(
        factory = PeliculasVistasViewModelFactory(firestore, auth)
    )
    val peliculasVistas by peliculasVistasViewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        firestore.getFavoriteMovies(userId).collect { peliculas ->
            peliculasFavoritas = peliculas
        }
        peliculasVistasViewModel.loadVistas()
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
                            text = "$userName",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black.copy(alpha = 0.8f)
                        )

                        if (userPhotoUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user?.photoUrl)
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
                if (userPhotoUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(userPhotoUrl)
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
                text = userName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(50.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Películas Vistas",
                    fontSize = 16.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val peliculasVistasViewModel: PeliculasVistasViewModel = viewModel(
                        factory = PeliculasVistasViewModelFactory(firestore, auth)
                    )
                    val peliculasVistas by peliculasVistasViewModel.uiState.collectAsState()

                    LaunchedEffect(userId) {
                        peliculasVistasViewModel.loadVistasUsuario(userId)
                    }

                    if (peliculasVistas.peliculas.isEmpty()) {
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

            Spacer(modifier = Modifier.height(5.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Reviews",
                    fontSize = 16.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val reviewsUsuarioViewModel: ReviewsUsuarioViewModel = remember {
                        ReviewsUsuarioViewModel(firestore, auth)
                    }
                    val reviewsState by reviewsUsuarioViewModel.uiState.collectAsState()

                    LaunchedEffect(userId) {
                        reviewsUsuarioViewModel.loadReviewsUsuario(userId)
                    }

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