package com.example.hacksprintpokedex.domain.usecase

import com.example.hacksprintpokedex.domain.model.Pokemon
import com.example.hacksprintpokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(limit: Int): List<Pokemon> {
        return pokemonRepository.getPokemonList(limit)
    }

}