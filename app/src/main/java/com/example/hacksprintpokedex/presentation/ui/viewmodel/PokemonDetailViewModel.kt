package com.example.hacksprintpokedex.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hacksprintpokedex.domain.model.PokemonDetail
import com.example.hacksprintpokedex.domain.usecase.GetPokemonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PokemonDetailUiState {
    data class Success(val pokemonDetail: PokemonDetail) : PokemonDetailUiState()
    data class Error(val message: String) : PokemonDetailUiState()
    object Empty : PokemonDetailUiState()
}

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<PokemonDetailUiState>(PokemonDetailUiState.Empty)
    val uiState: StateFlow<PokemonDetailUiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun load(pokemonName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val pokemonDetail = getPokemonDetailUseCase(pokemonName)
                _uiState.value = PokemonDetailUiState.Success(pokemonDetail)
            } catch (e: Exception) {
                _uiState.value = PokemonDetailUiState.Error(e.message ?: "An unknown error occurred")
            } finally {
                _isLoading.value = false
            }
        }
    }
}