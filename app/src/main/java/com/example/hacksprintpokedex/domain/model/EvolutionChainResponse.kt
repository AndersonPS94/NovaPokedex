package com.example.hacksprintpokedex.domain.model

import com.google.gson.annotations.SerializedName

data class EvolutionChainResponse(
    @SerializedName("chain")
    val chain: ChainLink
) {
    data class ChainLink(
        @SerializedName("species")
        val species: Species,

        @SerializedName("evolves_to")
        val evolvesTo: List<ChainLink>
    )

    data class Species(
        @SerializedName("name")
        val name: String,

        @SerializedName("url")
        val url: String
    )
}
