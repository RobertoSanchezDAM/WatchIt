package com.example.robertosanchez.watchinit.repositories.models

data class CreditsResponse(
    val cast: List<CastMember>,
    val crew: List<CrewMember>
)

data class CastMember(
    val id: Int,
    val name: String,
    val character: String,
    val profile_path: String?
)

data class CrewMember(
    val id: Int,
    val name: String,
    val job: String,
    val profile_path: String?
) 