package com.example.hacksprintpokedex.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hacksprintpokedex.domain.model.PokemonDetail
import com.example.hacksprintpokedex.domain.repository.PokemonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private val _pokemonList = MutableLiveData<List<PokemonDetail>>()
    val pokemonList: LiveData<List<PokemonDetail>> = _pokemonList

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> = _isLoading

    fun loadPokemons() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val basicPokemons = repository.getPokemonList(380)
                val detailedPokemonList = basicPokemons.map { pokemon ->
                    async { repository.getPokemonDetail(pokemon.name) }
                }.awaitAll()

                _pokemonList.postValue(detailedPokemonList)
            } catch (e: Exception) {
                e.printStackTrace()
                _pokemonList.postValue(emptyList())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}