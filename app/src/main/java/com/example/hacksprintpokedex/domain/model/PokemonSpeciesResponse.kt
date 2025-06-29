package com.example.hacksprintpokedex.domain.model

import com.google.gson.annotations.SerializedName

data class PokemonSpeciesResponse(
    @SerializedName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>,

    @SerializedName("gender_rate")
    val genderRate: Int,

    @SerializedName("generation")
    val generation: Generation,

    @SerializedName("base_happiness")
    val baseHappiness: Int,

    @SerializedName("evolution_chain")
    val evolutionChain: EvolutionChainUrl
)

data class EvolutionChainUrl(
    @SerializedName("url")
    val url: String
)

data class FlavorTextEntry(
    @SerializedName("flavor_text")
    val flavorText: String,

    @SerializedName("language")
    val language: Language
)

data class Language(
    @SerializedName("name")
    val name: String
)

data class Generation(
    @SerializedName("name")
    val name: String
)
