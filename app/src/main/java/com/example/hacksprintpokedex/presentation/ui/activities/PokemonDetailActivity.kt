package com.example.hacksprintpokedex.presentation.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hacksprintpokedex.R
import com.example.hacksprintpokedex.databinding.ActivityPokemonDetailBinding
import com.example.hacksprintpokedex.presentation.ui.viewmodel.PokemonDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonDetailBinding
    private val viewModel: PokemonDetailViewModel by viewModels()

    private var currentPokemonId: Int = 1

    private var normalImageUrl: String? = null
    private var shinyImageUrl: String? = null

    private var showingShiny = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        currentPokemonId = intent.getIntExtra("pokemon_id", 1)

        setupObservers()
        setupButtons()
        setupImageClick()

        viewModel.loadPokemon(currentPokemonId.toString())
    }

    private fun setupObservers() {
        viewModel.pokemon.observe(this) { pokemon ->
            binding.pokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
            binding.pokemonNumber.text = "#${pokemon.id}"

            val firstType = pokemon.types.getOrNull(0) ?: ""
            val color = getTypeColor(firstType)

            binding.pokemonType1.apply {
                text = firstType.replaceFirstChar { it.uppercase() }
                backgroundTintList = android.content.res.ColorStateList.valueOf(color)
                visibility = if (firstType.isNotEmpty()) android.view.View.VISIBLE else android.view.View.GONE
            }

            val secondType = pokemon.types.getOrNull(1)
            binding.pokemonType2.apply {
                text = secondType?.replaceFirstChar { it.uppercase() } ?: ""
                visibility = if (secondType != null) android.view.View.VISIBLE else android.view.View.GONE
                backgroundTintList = android.content.res.ColorStateList.valueOf(getTypeColor(secondType ?: ""))
            }

            binding.typeBackgroundLayout.setBackgroundColor(color)

            normalImageUrl = pokemon.imageUrl
            shinyImageUrl = pokemon.shinyImageUrl ?: normalImageUrl

            showingShiny = false
            loadPokemonImage(normalImageUrl)

            binding.pokemonDescription.text = pokemon.description
            binding.pokemonRegion.text = "${pokemon.region}"
            binding.statWeightValue.text = "${pokemon.weight} kg"
            binding.statHeightValue.text = "${pokemon.height} m"
            binding.statAbilityValue.text = pokemon.ability

            if (pokemon.genderRate == -1) {
                binding.pokemonGender.text = "Genderless"
                binding.maleBar.layoutParams = android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0f)
                binding.femaleBar.layoutParams = android.widget.LinearLayout.LayoutParams(0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 0f)
            } else {
                val malePercent = 100 - pokemon.genderRate
                val femalePercent = pokemon.genderRate
                binding.pokemonGender.text = "$malePercent% ♂  $femalePercent% ♀"

                val paramsMale = binding.maleBar.layoutParams as android.widget.LinearLayout.LayoutParams
                val paramsFemale = binding.femaleBar.layoutParams as android.widget.LinearLayout.LayoutParams
                paramsMale.weight = malePercent.toFloat()
                paramsFemale.weight = femalePercent.toFloat()
                binding.maleBar.layoutParams = paramsMale
                binding.femaleBar.layoutParams = paramsFemale
            }
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupButtons() {
        binding.prevButton.setOnClickListener {
            if (currentPokemonId > 1) {
                currentPokemonId--
                viewModel.loadPokemon(currentPokemonId.toString())
            }
        }

        binding.nextButton.setOnClickListener {
            currentPokemonId++
            viewModel.loadPokemon(currentPokemonId.toString())
        }
    }

    private fun setupImageClick() {
        binding.pokemonImage.setOnClickListener {
            showingShiny = !showingShiny
            val urlToLoad = if (showingShiny) shinyImageUrl else normalImageUrl
            loadPokemonImage(urlToLoad)
        }
    }

    private fun loadPokemonImage(url: String?) {
        if (url == null) {
            binding.pokemonImage.setImageResource(R.drawable.bulabasaur)
            return
        }

        Glide.with(this)
            .asGif()
            .load(url)
            .error(
                Glide.with(this)
                    .load(url)
            )
            .into(binding.pokemonImage)
    }

    private fun getTypeColor(type: String): Int {
        return when (type.lowercase()) {
            "normal" -> getColor(R.color.type_normal)
            "fire" -> getColor(R.color.type_fire)
            "water" -> getColor(R.color.type_water)
            "grass" -> getColor(R.color.type_grass)
            "electric" -> getColor(R.color.type_electric)
            "ice" -> getColor(R.color.type_ice)
            "fighting" -> getColor(R.color.type_fighting)
            "poison" -> getColor(R.color.type_poison)
            "ground" -> getColor(R.color.type_ground)
            "flying" -> getColor(R.color.type_flying)
            "psychic" -> getColor(R.color.type_psychic)
            "bug" -> getColor(R.color.type_bug)
            "rock" -> getColor(R.color.type_rock)
            "ghost" -> getColor(R.color.type_ghost)
            "dragon" -> getColor(R.color.type_dragon)
            "dark" -> getColor(R.color.type_dark)
            "steel" -> getColor(R.color.type_steel)
            "fairy" -> getColor(R.color.type_fairy)
            else -> getColor(R.color.type_default)
        }
    }
}
