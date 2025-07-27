package com.example.hacksprintpokedex.data.model

data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val types: List<TypeSlot>,
    val height: Int,
    val weight: Int,
    val abilities: List<AbilitySlot>
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
