package com.anderson.hacksprintpokedex.presentation.ui.viewmodel

import com.anderson.hacksprintpokedex.domain.model.Pokemon

sealed class PokemonListUiState {
    object Loading : PokemonListUiState()
    data class Success(val pokemonList: List<Pokemon>) : PokemonListUiState()
    data class Error(val message: String) : PokemonListUiState()
}