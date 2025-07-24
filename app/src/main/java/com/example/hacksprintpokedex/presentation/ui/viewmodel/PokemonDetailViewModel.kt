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
        "farfetch'd" to "farfetchd",
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
        "flabébé" to "flabebe",
        "type.null" to "typenull",
        "mr.rime" to "mrrime",
        "sirfetchd" to "sirfetchd",
        "sirfetch'd" to "sirfetchd",
        "mime jr" to "mimejr",
        "tornadus-incarnate" to "tornadus",
        "tornadus-therian" to "tornadustherian",
        "thundurus-incarnate" to "thundurus",
        "thundurus-therian" to "thundurustherian",
        "landorus-incarnate" to "landorus",
        "landorus-therian" to "landorustherian",
        "keldeo-resolute" to "keldeo",
        "meloetta-aria" to "meloetta",
        "meloetta-pirouette" to "meloettapirouette",
        "meowstic-male" to "meowstic",
        "meowstic-female" to "meowsticf",
        "greninja-battle-bond" to "greninja",
        "greninja-ash" to "greninja",
        "zygarde-10" to "zygarde10",
        "zygarde-50" to "zygarde",
        "zygarde-complete" to "zygardec",
        "oricorio-baile" to "oricorio",
        "oricorio-pom-pom" to "oricoriopompom",
        "oricorio-pau" to "oricoriopau",
        "oricorio-sensu" to "oricoriosensu",
        "lycanroc-midday" to "lycanroc",
        "lycanroc-midnight" to "lycanrocmidnight",
        "lycanroc-dusk" to "lycanrocdusk",
        "wishiwashi-school" to "wishiwashischool",
        "minior-red-meteor" to "minior",
        "minior-orange-meteor" to "minior",
        "minior-yellow-meteor" to "minior",
        "minior-green-meteor" to "minior",
        "minior-blue-meteor" to "minior",
        "minior-indigo-meteor" to "minior",
        "minior-violet-meteor" to "minior",
        "minior-red" to "minior",
        "minior-orange" to "minior",
        "minior-yellow" to "minior",
        "minior-green" to "minior",
        "minior-blue" to "minior",
        "minior-indigo" to "minior",
        "minior-violet" to "minior",
        "mimikyu-busted" to "mimikyu",
        "magearna-original" to "magearna",
        "toxtricity-low-key" to "toxtricitylowkey",
        "eiscue-noice" to "eiscuenoice",
        "zacian-crowned" to "zacian",
        "zamazenta-crowned" to "zamazenta",
        "eternatus-eternamax" to "eternatus",
        "urshifu-rapid-strike" to "urshifurapidstrike",
        "urshifu-single-strike" to "urshifu",
        "zarude-dada" to "zarudedada",
        "calyrex-shadow" to "calyrexshadow",
        "calyrex-ice" to "calyrexice",
        // Mr Mime variants
        "mr.mime" to "mrmime",
        "mr. mime" to "mrmime",

        // Mime Jr (não muda)
        "mimejr" to "mimejr",

        // Farfetch'd pode aparecer como farfetchd
        "farfetchd" to "farfetchd",
        "farfetch'd" to "farfetchd",

        // Type: Null
        "type.null" to "typenull",

        // Jangmo-o variants
        "jangmo-o" to "jangmoo",
        "hakamo-o" to "hakamo-o",
        "kommo-o" to "kommoo",

        // Tapu variants (sem espaço)
        "tapu koko" to "tapukoko",
        "tapu lele" to "tapulele",
        "tapu bulu" to "tapubulu",
        "tapu fini" to "tapufini",

        // Ho-oh sem hífen
        "ho-oh" to "hooh",

        // Porygon-Z
        "porygon-z" to "porygonz",

        // Nidoran macho e fêmea — cuidado: no input pode vir com '-', '♂' ou '♀'
        "nidoran♀" to "nidoranf",
        "nidoran♀-" to "nidoranf",
        "nidoran-f" to "nidoranf",
        "nidoran-female" to "nidoranf",
        "nidoran-fem" to "nidoranf",
        "nidoran-m" to "nidoranm",
        "nidoran♂" to "nidoranm",
        "nidoran♂-" to "nidoranm",
        "nidoran-male" to "nidoranm",
        "nidoran-m" to "nidoranm",

        // Flabébé sem acento
        "flabébé" to "flabebe",
        "flabebe" to "flabebe",

        // Galarian forms (exemplos)
        "rattata-galar" to "rattatagalar",
        "raticate-galar" to "raticategalar",
        "zigzagoon-galar" to "zigzagoongalar",
        "linoone-galar" to "linoonegalar",

        // Alolan forms (exemplos)
        "raichu-alola" to "raichualola",
        "sandshrew-alola" to "sandshrewalola",
        "vulpix-alola" to "vulpixalola",

        // Variantes comuns adicionais
        "deoxys-attack" to "deoxysattack",
        "deoxys-defense" to "deoxysdefense",
        "deoxys-speed" to "deoxysspeed",
        "giratina-origin" to "giratinaorigin",
        "castform-sunny" to "castformsunny",
        "castform-rainy" to "castformrainy",
        "castform-snowy" to "castformsnowy",
        "basculin-blue-striped" to "basculinbluestriped",
        "basculin-red-striped" to "basculinredstriped",
        "darmanitan-zen" to "darmanitanzen",
        "tornadus-therian" to "tornadustherian",
        "thundurus-therian" to "thundurustherian",
        "landorus-therian" to "landorustherian",
        "kyurem-black" to "kyuremblack",
        "kyurem-white" to "kyuremwhite",
        "oricorio-pom-pom" to "oricoriopompom",
        "lycanroc-midnight" to "lycanrocmidnight",
        "lycanroc-dusk" to "lycanrocdusk",
        "wishiwashi-school" to "wishiwashischool",
        "minior-red-meteor" to "minior",
        "mimikyu-busted" to "mimikyu",
        "toxtricity-low-key" to "toxtricitylowkey",
        "urshifu-rapid-strike" to "urshifurapidstrike",
        "urshifu-single-strike" to "urshifu",
        "calyrex-shadow" to "calyrexshadow",
        "calyrex-ice" to "calyrexice"
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
