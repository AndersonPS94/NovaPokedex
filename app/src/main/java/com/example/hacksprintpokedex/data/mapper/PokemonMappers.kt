// app/src/main/java/com/example/hacksprintpokedex/data/mapper/PokemonMappers.kt
package com.example.hacksprintpokedex.data.mapper

import com.example.hacksprintpokedex.data.model.PokemonDetailResponse
import com.example.hacksprintpokedex.data.local.entities.Pokemon as LocalPokemon // Alias para evitar conflito com domain.model.Pokemon

import com.example.hacksprintpokedex.domain.model.Pokemon // Importa o modelo de domínio UNIFICADO Pokemon

// Mapeia de PokemonDetailResponse (da API) para LocalPokemon (entidade do Room)
fun mapPokemonDetailResponseToLocalPokemon(detailResponse: PokemonDetailResponse): LocalPokemon {
    val id = detailResponse.id
    val name = detailResponse.name
    // URL da imagem oficial (artwork)
    val imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png"
    // URL da imagem shiny (showdown animated)
    val shinyImageUrl = "https://play.pokemonshowdown.com/sprites/ani-shiny/${name.lowercase()}.gif"

    val types = detailResponse.types.map { it.type.name } // Mapeia para List<String>
    val weight = detailResponse.weight / 10.0 // Converte para kg
    val height = detailResponse.height / 10.0 // Converte para metros

    // Mapeamento de outras informações, se necessário, ou deixe valores padrão
    val ability = detailResponse.abilities.firstOrNull()?.ability?.name ?: "Unknown"
    // Descrição e região geralmente vêm de PokemonSpeciesResponse, não de PokemonDetailResponse.
    // Se você estiver mapeando apenas de PokemonDetailResponse, terá que usar valores padrão ou buscá-los de outro lugar.
    val description = "" // Valor padrão, a menos que você passe a speciesResponse aqui
    val region = "" // Valor padrão
    val genderRate = 0 // Valor padrão

    return LocalPokemon(
        id = id,
        name = name,
        imageUrl = imageUrl,
        shinyImageUrl = shinyImageUrl,
        types = types, // <--- AGORA PASSA List<String>
        weight = weight,
        height = height,
        ability = ability,
        region = region,
        description = description,
        genderRate = genderRate
    )
}

// Mapeia de LocalPokemon (entidade do Room) para Pokemon (modelo de domínio unificado)
fun LocalPokemon.toDomainPokemon(): Pokemon {
    return Pokemon(
        id = this.id,
        name = this.name,
        imageUrl = this.imageUrl,
        shinyImageUrl = this.shinyImageUrl,
        types = this.types, // <--- Assumindo que LocalPokemon.types é List<String>
        weight = this.weight,
        height = this.height,
        ability = this.ability,
        region = this.region,
        description = this.description,
        genderRate = this.genderRate
    )
}