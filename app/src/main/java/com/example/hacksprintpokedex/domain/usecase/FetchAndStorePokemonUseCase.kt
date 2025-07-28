package com.example.hacksprintpokedex.domain.usecase

import com.example.hacksprintpokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class FetchAndStorePokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(limit: Int) {
        pokemonRepository.fetchAndStorePokemonList(limit)
    }
}