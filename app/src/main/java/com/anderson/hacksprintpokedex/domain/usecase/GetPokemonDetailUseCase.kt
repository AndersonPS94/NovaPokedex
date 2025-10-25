package com.anderson.hacksprintpokedex.domain.usecase

import com.anderson.hacksprintpokedex.domain.model.PokemonDetail
import com.anderson.hacksprintpokedex.domain.repository.PokemonRepository
import javax.inject.Inject

class GetPokemonDetailUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(nameOrId: String, language: String): PokemonDetail {
        return repository.getPokemonDetail(nameOrId, language)
    }
}
