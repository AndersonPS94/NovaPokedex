package com.example.hacksprintpokedex.presentation.ui.activities

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.hacksprintpokedex.R
import com.example.hacksprintpokedex.data.remote.api.PokeApiService
import com.example.hacksprintpokedex.databinding.ActivityPokemonDetailBinding
import com.example.hacksprintpokedex.data.model.EvolutionChainResponse
import com.example.hacksprintpokedex.data.model.PokemonDetailResponse
import com.example.hacksprintpokedex.data.model.PokemonSpeciesResponse
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.res.ColorStateList

class PokemonDetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPokemonDetailBinding.inflate(layoutInflater) }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val pokeApiService by lazy {
        retrofit.create(PokeApiService::class.java)
    }

    private var currentPokemonId: Int = 1

    private val typeColorMap = mapOf(
        "normal" to "#A8A77A", "fire" to "#EE8130", "water" to "#6390F0",
        "electric" to "#F7D02C", "grass" to "#7AC74C", "ice" to "#96D9D6",
        "fighting" to "#C22E28", "poison" to "#A33EA1", "ground" to "#E2BF65",
        "flying" to "#A98FF3", "psychic" to "#F95587", "bug" to "#A6B91A",
        "rock" to "#B6A136", "ghost" to "#735797", "dragon" to "#6F35FC",
        "dark" to "#705746", "steel" to "#B7B7CE", "fairy" to "#D685AD"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pokemonNameFromIntent = intent.getStringExtra("pokemon_name") ?: "bulbasaur"
        loadPokemonByName(pokemonNameFromIntent.lowercase())

        binding.prevButton.setOnClickListener {
            if (currentPokemonId > 1) {
                loadPokemonById(currentPokemonId - 1)
            }
        }

        binding.nextButton.setOnClickListener {
            loadPokemonById(currentPokemonId + 1)
        }
    }

    private fun loadPokemonByName(name: String) {
        lifecycleScope.launch {
            try {
                val detailResponse = pokeApiService.getPokemon(name)
                val speciesResponse = pokeApiService.getPokemonSpecies(name)

                if (detailResponse.isSuccessful && speciesResponse.isSuccessful) {
                    val pokemonDetail = detailResponse.body()!!
                    val pokemonSpecies = speciesResponse.body()!!
                    currentPokemonId = pokemonDetail.id

                    val evolutionUrl = pokemonSpecies.evolutionChain.url
                    val chainId = evolutionUrl.split("/").filter { it.isNotEmpty() }.last().toInt()
                    val evolutionResponse = pokeApiService.getEvolutionChain(chainId)

                    if (evolutionResponse.isSuccessful) {
                        val evolutionChain = evolutionResponse.body()
                        preencherUI(pokemonDetail, pokemonSpecies, evolutionChain)
                    } else {
                        preencherUI(pokemonDetail, pokemonSpecies, null)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadPokemonById(id: Int) {
        lifecycleScope.launch {
            try {
                val detailResponse = pokeApiService.getPokemon(id.toString())
                val speciesResponse = pokeApiService.getPokemonSpecies(id.toString())

                if (detailResponse.isSuccessful && speciesResponse.isSuccessful) {
                    val pokemonDetail = detailResponse.body()!!
                    val pokemonSpecies = speciesResponse.body()!!
                    currentPokemonId = pokemonDetail.id

                    val evolutionUrl = pokemonSpecies.evolutionChain.url
                    val chainId = evolutionUrl.split("/").filter { it.isNotEmpty() }.last().toInt()
                    val evolutionResponse = pokeApiService.getEvolutionChain(chainId)

                    if (evolutionResponse.isSuccessful) {
                        val evolutionChain = evolutionResponse.body()
                        preencherUI(pokemonDetail, pokemonSpecies, evolutionChain)
                    } else {
                        preencherUI(pokemonDetail, pokemonSpecies, null)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun preencherUI(
        detail: PokemonDetailResponse,
        species: PokemonSpeciesResponse,
        evolutionChain: EvolutionChainResponse?
    ) {
        binding.pokemonName.text = detail.name.replaceFirstChar { it.uppercase() }
        binding.pokemonNumber.text = "#${detail.id.toString().padStart(3, '0')}"
        val imageUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${detail.id}.png"
        Picasso.get().load(imageUrl).into(binding.pokemonImage)

        val types = detail.types.sortedBy { it.slot }.map { it.type.name.lowercase() }
        val type1 = types.getOrNull(0)
        val type2 = types.getOrNull(1)

        binding.pokemonType1.text = type1?.replaceFirstChar { it.uppercase() } ?: ""
        val color1 = typeColorMap[type1] ?: "#777777"
        binding.pokemonType1.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color1))
        binding.typeBackgroundLayout.setBackgroundColor(Color.parseColor(color1))

        if (type2 != null) {
            binding.pokemonType2.text = type2.replaceFirstChar { it.uppercase() }
            val color2 = typeColorMap[type2] ?: "#777777"
            binding.pokemonType2.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color2))
            binding.pokemonType2.visibility = View.VISIBLE
        } else {
            binding.pokemonType2.visibility = View.GONE
        }

        binding.statWeightIcon.setImageResource(R.drawable.business)
        binding.statWeightLabel.text = "Weight"
        binding.statWeightValue.text = "${detail.weight / 10.0} kg"

        binding.statHeightIcon.setImageResource(R.drawable.resize)
        binding.statHeightLabel.text = "Height"
        binding.statHeightValue.text = "${detail.height / 10.0} m"

        val abilityName = detail.abilities.firstOrNull()?.ability?.name ?: "Unknown"
        binding.statAbilityIcon.setImageResource(R.drawable.thunder)
        binding.statAbilityLabel.text = "Ability"
        binding.statAbilityValue.text = abilityName.replaceFirstChar { it.uppercase() }

        val regionName = species.generation.name.replace("-", " ").replaceFirstChar { it.uppercase() }
        binding.pokemonRegion.text = "Region: $regionName"

        val flavorText = species.flavorTextEntries.firstOrNull {
            it.language.name == "en" || it.language.name == "pt"
        }?.flavorText?.replace("\n", " ")?.replace("\u000c", " ") ?: "Description not available."
        binding.pokemonDescription.text = flavorText


        // Gênero
        val genderRate = species.genderRate
        if (genderRate == -1) {
            binding.pokemonGender.text = "Genderless"
            binding.maleBar.isVisible = false
            binding.femaleBar.isVisible = false
        } else {
            val malePercent = ((8 - genderRate) / 8.0) * 100
            val femalePercent = 100 - malePercent

            binding.pokemonGender.text = String.format("%.1f%% Male  %.1f%% Female", malePercent, femalePercent)

            val maleParams = binding.maleBar.layoutParams as LinearLayout.LayoutParams
            maleParams.weight = malePercent.toFloat()
            binding.maleBar.layoutParams = maleParams
            binding.maleBar.setBackgroundColor(Color.parseColor("#36A2EB"))
            binding.maleBar.isVisible = true

            val femaleParams = binding.femaleBar.layoutParams as LinearLayout.LayoutParams
            femaleParams.weight = femalePercent.toFloat()
            binding.femaleBar.layoutParams = femaleParams
            binding.femaleBar.setBackgroundColor(Color.parseColor("#FF6384"))
            binding.femaleBar.isVisible = true
        }
    }

}
