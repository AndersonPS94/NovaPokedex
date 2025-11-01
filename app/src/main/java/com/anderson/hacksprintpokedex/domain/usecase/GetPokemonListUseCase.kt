package com.anderson.hacksprintpokedex.domain.usecase

import com.anderson.hacksprintpokedex.domain.model.Pokemon
import com.anderson.hacksprintpokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke(): Flow<List<Pokemon>> {
        return pokemonRepository.getPokemonList()
    }
}
