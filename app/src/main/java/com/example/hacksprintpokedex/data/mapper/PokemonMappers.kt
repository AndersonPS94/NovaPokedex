package com.example.hacksprintpokedex.data.mapper

import com.example.hacksprintpokedex.data.model.PokemonDetailResponse
import com.example.hacksprintpokedex.data.local.entities.Pokemon as LocalPokemon // Alias para evitar conflito com domain.model.Pokemon

import com.example.hacksprintpokedex.domain.model.Pokemon // Importa o modelo de domínio UNIFICADO Pokemon

fun mapPokemonDetailResponseToLocalPokemon(detailResponse: PokemonDetailResponse): LocalPokemon {
    val id = detailResponse.id
    val name = detailResponse.name
    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"
    val shinyImageUrl = "https://play.pokemonshowdown.com/sprites/ani-shiny/${name.lowercase()}.gif"

    val types = detailResponse.types.map { it.type.name }
    val weight = detailResponse.weight / 10.0
    val height = detailResponse.height / 10.0

    val ability = detailResponse.abilities.firstOrNull()?.ability?.name ?: "Unknown"
    val description = ""
    val region = ""
    val genderRate = 0

    return LocalPokemon(
        id = id,
        name = name,
        imageUrl = imageUrl,
        shinyImageUrl = shinyImageUrl,
        types = types,
        weight = weight,
        height = height,
        ability = ability,
        region = region,
        description = description,
        genderRate = genderRate
    )
}

fun LocalPokemon.toDomainPokemon(): Pokemon {
    return Pokemon(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        shinyImageUrl = this.shinyImageUrl,
        types = this.types,
        weight = this.weight,
        height = this.height,
        ability = this.ability,
        region = this.region,
        description = this.description,
        genderRate = this.genderRate
    )
}