package com.example.hacksprintpokedex.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hacksprintpokedex.domain.model.Pokemon
import com.example.hacksprintpokedex.domain.usecase.FetchAndStorePokemonUseCase
import com.example.hacksprintpokedex.domain.usecase.GetPokemonListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val fetchAndStorePokemonUseCase: FetchAndStorePokemonUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonListUiState>(PokemonListUiState.Loading)
    val uiState: StateFlow<PokemonListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getPokemonListUseCase()
                .catch { e -> _uiState.value = PokemonListUiState.Error(e.message ?: "Unknown error") }
                .collect { pokemonList ->
                    _uiState.value = PokemonListUiState.Success(pokemonList)
                }
        }
        fetchPokemonsFromNetwork()
    }

    fun fetchPokemonsFromNetwork() {
        viewModelScope.launch {
            if (_uiState.value !is PokemonListUiState.Success || (_uiState.value as PokemonListUiState.Success).pokemonList.isEmpty()) {
                _uiState.value = PokemonListUiState.Loading
            }
            try {
                fetchAndStorePokemonUseCase(385)
            } catch (e: Exception) {
                if (_uiState.value is PokemonListUiState.Success && (_uiState.value as PokemonListUiState.Success).pokemonList.isEmpty()) {
                    _uiState.value = PokemonListUiState.Error(e.message ?: "Network error fetching data")
                } else if (_uiState.value is PokemonListUiState.Loading) {
                    _uiState.value = PokemonListUiState.Error(e.message ?: "Network error during initial load")
                }
            }
        }
    }
}