package com.example.hacksprintpokedex.domain.usecase

import com.example.hacksprintpokedex.domain.model.Pokemon
import com.example.hacksprintpokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow // Importe o Flow
import javax.inject.Inject

class GetPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke(): Flow<List<Pokemon>> {
        return pokemonRepository.getPokemonList()
    }
}