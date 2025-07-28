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
        // Começa a observar o banco de dados Room imediatamente
        viewModelScope.launch {
            getPokemonListUseCase()
                .catch { e -> _uiState.value = PokemonListUiState.Error(e.message ?: "Unknown error") }
                .collect { pokemonList ->
                    _uiState.value = PokemonListUiState.Success(pokemonList)
                }
        }
        // Dispara a busca da rede e o armazenamento no Room (uma vez ao iniciar)
        fetchPokemonsFromNetwork()
    }

    fun fetchPokemonsFromNetwork() {
        viewModelScope.launch {
            // Pode ser bom mostrar um loading apenas se a lista local estiver vazia
            // ou se for um pull-to-refresh
            if (_uiState.value !is PokemonListUiState.Success || (_uiState.value as PokemonListUiState.Success).pokemonList.isEmpty()) {
                _uiState.value = PokemonListUiState.Loading
            }
            try {
                fetchAndStorePokemonUseCase(385)
            } catch (e: Exception) {
                // Se der erro de rede, apenas notifique se não houver dados locais para mostrar
                if (_uiState.value is PokemonListUiState.Success && (_uiState.value as PokemonListUiState.Success).pokemonList.isEmpty()) {
                    _uiState.value = PokemonListUiState.Error(e.message ?: "Network error fetching data")
                } else if (_uiState.value is PokemonListUiState.Loading) {
                    _uiState.value = PokemonListUiState.Error(e.message ?: "Network error during initial load")
                }
            }
        }
    }
}