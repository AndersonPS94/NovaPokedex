package com.example.hacksprintpokedex.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hacksprintpokedex.domain.model.PokemonDetail
import com.example.hacksprintpokedex.domain.usecase.GetPokemonDetailUseCase
import com.example.hacksprintpokedex.presentation.ui.navigation.NavigationTarget
import com.example.hacksprintpokedex.presentation.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {

    private val _pokemonDetail = MutableLiveData<PokemonDetail>()
    val pokemonDetail: LiveData<PokemonDetail> = _pokemonDetail

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _navigateEvent = MutableLiveData<Event<NavigationTarget>>()
    val navigateEvent: LiveData<Event<NavigationTarget>> = _navigateEvent

    fun loadPokemon(name: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val detail = getPokemonDetailUseCase(name)
                _pokemonDetail.postValue(detail)
            } catch (e: Exception) {
                _error.postValue(e.message ?: "Erro desconhecido")
                _navigateEvent.postValue(Event(NavigationTarget.ErrorActivity))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
