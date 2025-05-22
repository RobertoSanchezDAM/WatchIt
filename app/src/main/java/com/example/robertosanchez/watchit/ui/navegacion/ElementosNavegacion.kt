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
data class ListaLargaPeliculas(val tipo: String = "")

@Serializable
object Perfil

@Serializable
data class Detail(val id: Int)

@Serializable
object Busqueda

@Serializable
data class BusquedaNombre(val pelicula: String)

@Serializable
object ListaFecha

@Serializable
data class Fecha(val fecha: Int)

@Serializable
object ListaGenero

@Serializable
data class Genero(val genero: Int)

@Serializable
object ProximosEstrenos

@Serializable
object EnCines