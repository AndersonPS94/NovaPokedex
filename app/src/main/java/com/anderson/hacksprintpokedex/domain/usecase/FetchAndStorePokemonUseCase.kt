package com.anderson.hacksprintpokedex.domain.usecase

import com.anderson.hacksprintpokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class FetchAndStorePokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke() {
        pokemonRepository.fetchAndStorePokemonList()
    }
}