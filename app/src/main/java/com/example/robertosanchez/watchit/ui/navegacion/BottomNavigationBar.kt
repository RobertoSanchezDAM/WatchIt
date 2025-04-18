package com.example.robertosanchez.watchit.ui.navegacion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Search : BottomNavItem("search", "Buscar", Icons.Default.Search)
    object Watch : BottomNavItem("watch", "Ver", Icons.Default.Star)
    object Home : BottomNavItem("home", "Inicio", Icons.Default.Home)
    object Profile : BottomNavItem("profile", "Perfil", Icons.Default.Person)
    object Logout : BottomNavItem("logout", "Salir", Icons.AutoMirrored.Outlined.ExitToApp)
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    onLogoutClick: () -> Unit = {}
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF3B82F6))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Elementos de la izquierda
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NavigationItem(
                    item = BottomNavItem.Search,
                    isSelected = currentRoute == BottomNavItem.Search.route,
                    onItemClick = { navController.navigate(BottomNavItem.Search.route) }
                )
                NavigationItem(
                    item = BottomNavItem.Watch,
                    isSelected = currentRoute == BottomNavItem.Watch.route,
                    onItemClick = { navController.navigate(BottomNavItem.Watch.route) }
                )
            }

            // Botón central (Home)
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

            // Elementos de la derecha
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                NavigationItem(
                    item = BottomNavItem.Profile,
                    isSelected = currentRoute == BottomNavItem.Profile.route,
                    onItemClick = { navController.navigate(BottomNavItem.Profile.route) }
                )
                NavigationItem(
                    item = BottomNavItem.Logout,
                    isSelected = false,
                    onItemClick = onLogoutClick
                )
            }
        }
    }
}

@Composable
private fun NavigationItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onItemClick: () -> Unit
) {
    val iconTint = if (isSelected) Color.White else Color.Black.copy(alpha = 0.6f)
    
    IconButton(
        onClick = onItemClick,
        modifier = Modifier.size(48.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
    }
} 