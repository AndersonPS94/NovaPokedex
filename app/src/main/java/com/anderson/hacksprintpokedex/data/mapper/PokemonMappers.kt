package com.anderson.hacksprintpokedex.data.mapper

import com.anderson.hacksprintpokedex.data.local.entities.Pokemon as LocalPokemon
import com.anderson.hacksprintpokedex.data.local.entities.PokemonDetailEntity
import com.anderson.hacksprintpokedex.domain.model.Pokemon
import com.anderson.hacksprintpokedex.domain.model.PokemonDetail

fun LocalPokemon.toDomainPokemon(): Pokemon {
    return Pokemon(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        shinyImageUrl = this.shinyImageUrl,
        officialArtworkUrl = this.officialArtworkUrl,
        types = this.types,
        weight = this.weight,
        height = this.height,
        ability = this.ability,
        region = this.region,
        description = this.description,
        genderRate = this.genderRate
    )
}

fun Pokemon.toLocalPokemon(): LocalPokemon {
    return LocalPokemon(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        shinyImageUrl = this.shinyImageUrl,
        officialArtworkUrl = this.officialArtworkUrl,
        types = this.types,
        weight = this.weight,
        height = this.height,
        ability = this.ability,
        region = this.region,
        description = this.description,
        genderRate = this.genderRate
    )
}

fun PokemonDetailEntity.toDomainDetail(): PokemonDetail {
    return PokemonDetail(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        shinyImageUrl = this.shinyImageUrl,
        officialArtworkUrl = this.officialArtworkUrl,
        types = this.types,
        weight = this.weight,
        height = this.height,
        ability = this.ability,
        region = this.region,
        description = this.description,
        genderRate = this.genderRate
    )
}

fun PokemonDetail.toLocalPokemon(): PokemonDetailEntity {
    return PokemonDetailEntity(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        shinyImageUrl = this.shinyImageUrl,
        officialArtworkUrl = this.officialArtworkUrl,
        types = this.types,
        weight = this.weight,
        height = this.height,
        ability = this.ability,
        region = this.region,
        description = this.description,
        genderRate = this.genderRate
    )
}
