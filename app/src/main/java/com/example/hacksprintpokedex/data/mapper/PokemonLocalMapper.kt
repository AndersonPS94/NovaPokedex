package com.example.hacksprintpokedex.data.mapper

import com.example.hacksprintpokedex.domain.model.PokemonDetail
import com.example.hacksprintpokedex.data.local.entities.Pokemon // Import the local entity

fun PokemonDetail.toLocalPokemon(): Pokemon {
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