package com.example.robertosanchez.proyectoapi.ui.screens.contrasenaOlvScreen


import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.robertosanchez.proyectoapi.data.AuthManager
import com.example.robertosanchez.proyectoapi.data.AuthRes
import com.example.robertosanchez.watchit.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ContrasenaOlvScreen(auth: AuthManager, navigateToLogin: () -> Unit) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo
        Image(
            painter = painterResource(id = R.drawable.pxfuel),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenido de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.8f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                label = { Text(text = "Correo electrónico") },
                value = email,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                onValueChange = { email = it }
            )

            Spacer(modifier = Modifier.height(30.dp))

            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        scope.launch {
                            forgotPassword(email, auth, context, navigateToLogin)
                        }
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF1ED760)
                    )
                ) {
                    Text(
                        text = "Recuperar Contraseña".uppercase(),
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

suspend fun forgotPassword(email: String, auth: AuthManager, context: Context, navigateToLogin: () -> Unit) {
    if (email.isNotEmpty()) {
        val res = withContext(Dispatchers.IO) {
            auth.resetPassword(email)
        }
        when (res) {
            is AuthRes.Success -> {
                Toast.makeText(
                    context,
                    "Se ha enviado un correo para restablecer la contraseña",
                    Toast.LENGTH_SHORT
                ).show()
                navigateToLogin()
            }

            is AuthRes.Error -> {
                Toast.makeText(context, "Error: ${res.errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        Toast.makeText(context, "Por favor, ingrese su correo electrónico", Toast.LENGTH_SHORT).show()
    }
}
