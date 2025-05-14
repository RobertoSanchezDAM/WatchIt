package com.example.robertosanchez.watchit.ui.navegacion

import kotlinx.serialization.Serializable

@Serializable
object Inicio

@Serializable
object Login

@Serializable
object ContraseñaOlv

@Serializable
object Registro

@Serializable
object Principal

@Serializable
data class ListaLargaPeliculas(val tipo: String = "Populares")

@Serializable
object Perfil

@Serializable
data class Detail(val id: Int)