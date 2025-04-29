package com.example.robertosanchez.watchit.ui.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

// Borde superior (TopAppBar)
class CustomShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            quadraticBezierTo(
                size.width / 2,
                size.height - 60f,
                0f,
                size.height
            )
            close()
        }
        return Outline.Generic(path)
    }
}

// Borde inferior (BottomNavigationBar)
class BottomBarCustomShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            moveTo(0f, size.height)
            lineTo(size.width, size.height)
            lineTo(size.width, 0f)
            quadraticBezierTo(
                size.width / 2,
                60f,
                0f,
                0f
            )
            close()
        }
        return Outline.Generic(path)
    }
} 