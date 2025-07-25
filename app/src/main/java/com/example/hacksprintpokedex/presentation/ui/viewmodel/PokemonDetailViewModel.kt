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
    private val useCase: GetPokemonDetailUseCase
) : ViewModel() {

    private val _pokemonDetail = MutableLiveData<PokemonDetail>()
    val pokemonDetail: LiveData<PokemonDetail> = _pokemonDetail

    private val _navigationTarget = MutableLiveData<Event<NavigationTarget>>()
    val navigationTarget: LiveData<Event<NavigationTarget>> = _navigationTarget

    fun load(name: String) = viewModelScope.launch {
        try {
            val pokemon = useCase(name)
            _pokemonDetail.postValue(pokemon)
        } catch (e: Exception) {
            _navigationTarget.postValue(Event(NavigationTarget.ErrorActivity))
        }
    }
}
