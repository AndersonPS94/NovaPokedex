package com.example.hacksprintpokedex.domain.usecase

import com.example.hacksprintpokedex.domain.model.PokemonDetail
import com.example.hacksprintpokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonDetailUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(nameOrId: String): PokemonDetail {
        return repository.getPokemonDetail(nameOrId)
    }
}
