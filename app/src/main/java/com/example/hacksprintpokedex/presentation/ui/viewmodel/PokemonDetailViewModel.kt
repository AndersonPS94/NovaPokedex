package com.example.hacksprintpokedex.presentation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hacksprintpokedex.domain.model.PokemonDetail
import com.example.hacksprintpokedex.domain.usecase.GetPokemonDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase
) : ViewModel() {

    private val _pokemon = MutableLiveData<PokemonDetail>()
    val pokemon: LiveData<PokemonDetail> = _pokemon

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val nameMap = mapOf(
        "mr.mime" to "mrmime",
        "mr. mime" to "mrmime",
        "mimejr" to "mimejr",
        "farfetchd" to "farfetchd",
        "type.null" to "typenull",
        "jangmo-o" to "jangmoo",
        "hakamo-o" to "hakamo-o",
        "kommo-o" to "kommoo",
        "tapu koko" to "tapukoko",
        "tapu lele" to "tapulele",
        "tapu bulu" to "tapubulu",
        "tapu fini" to "tapufini",
        "ho-oh" to "hooh",
        "porygon-z" to "porygonz",
        "nidoran♀" to "nidoranf",
        "nidoran♂" to "nidoranm",
        "flabébé" to "flabebe"
    )

    fun loadPokemon(nameOrId: String) {
        viewModelScope.launch {
            try {
                val data = getPokemonDetailUseCase(nameOrId)

                val rawName = data.name.lowercase()

                val formattedName = nameMap[rawName] ?: rawName
                    .replace(" ", "")
                    .replace(".", "")
                    .replace("'", "")
                    .replace("♀", "f")
                    .replace("♂", "m")
                    .replace("é", "e")

                val baseGifUrl = "https://play.pokemonshowdown.com/sprites/ani/"
                val baseShinyGifUrl = "https://play.pokemonshowdown.com/sprites/ani-shiny/"

                val normalGifUrl = "$baseGifUrl$formattedName.gif"
                val shinyGifUrl = "$baseShinyGifUrl$formattedName.gif"

                val updatedData = data.copy(
                    imageUrl = normalGifUrl,
                    shinyImageUrl = shinyGifUrl
                )

                _pokemon.value = updatedData
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            }
        }
    }
}
