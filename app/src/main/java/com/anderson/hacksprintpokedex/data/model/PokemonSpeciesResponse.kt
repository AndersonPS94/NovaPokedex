package com.anderson.hacksprintpokedex.data.model

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
    val evolutionChain: EvolutionChainUrl,

    val names: List<Name> // Added for translation
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

// This is now defined in TranslationModels.kt, but let's keep it here for self-containment if needed
// data class Language(
//     @SerializedName("name")
//     val name: String
// )

data class Generation(
    @SerializedName("name")
    val name: String
)
