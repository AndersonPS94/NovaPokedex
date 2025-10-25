package com.example.hacksprintpokedex.data.model

import com.google.gson.annotations.SerializedName

data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val types: List<TypeSlot>,
    val height: Int,
    val weight: Int,
    val abilities: List<AbilitySlot>,
    val sprites: Sprites
)

data class TypeSlot(
    val slot: Int,
    val type: TypeInfo
)

data class TypeInfo(
    val name: String
)

data class AbilitySlot(
    val ability: AbilityInfo
)

data class AbilityInfo(
    val name: String
)

data class Sprites(
    val other: Other
)

data class Other(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork
)

data class OfficialArtwork(
    @SerializedName("front_default")
    val frontDefault: String
)
