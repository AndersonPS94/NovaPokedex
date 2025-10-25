package com.example.hacksprintpokedex.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.example.hacksprintpokedex.R
import com.example.hacksprintpokedex.databinding.ActivityPokemonDetailBinding
import com.example.hacksprintpokedex.domain.model.PokemonDetail
import com.example.hacksprintpokedex.presentation.ui.viewmodel.PokemonDetailUiState
import com.example.hacksprintpokedex.presentation.ui.viewmodel.PokemonDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class PokemonDetailActivity : AppCompatActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var binding: ActivityPokemonDetailBinding
    private val viewModel: PokemonDetailViewModel by viewModels()

    private var allPokemonNames: List<String> = emptyList()
    private var currentIndex: Int = -1
    private var showingShiny = false
    private lateinit var currentPokemon: PokemonDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val pokemonNamesList = intent.getStringArrayListExtra("pokemonNamesList")
        val pokemonName = intent.getStringExtra("pokemonName")

        if (pokemonNamesList != null && pokemonName != null) {
            allPokemonNames = pokemonNamesList
            currentIndex = allPokemonNames.indexOfFirst { it.equals(pokemonName, ignoreCase = true) }

            if (currentIndex == -1) {
                Toast.makeText(this, "Pokémon not found in the list", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
            viewModel.load(allPokemonNames[currentIndex])
        } else if (pokemonName != null) {
            viewModel.load(pokemonName)
        } else {
            Toast.makeText(this, "Insufficient data to display Pokémon", Toast.LENGTH_SHORT)
                .show()
            finish()
            return
        }

        observeViewModel()
        setupNavigationButtons()
        setupImageToggle()
    }

    private fun observeViewModel() {
        viewModel.uiState
            .onEach { state ->
                when (state) {
                    is PokemonDetailUiState.Success -> {
                        currentPokemon = state.pokemonDetail
                        updateUI(currentPokemon)
                    }
                    is PokemonDetailUiState.Error -> {
                        Toast.makeText(this@PokemonDetailActivity, state.message, Toast.LENGTH_LONG).show()
                        val intent = Intent(this@PokemonDetailActivity, ErrorActivity::class.java).apply {
                            putExtra("errorMessage", state.message)
                        }
                        startActivity(intent)
                        finish()
                    }
                    is PokemonDetailUiState.Empty -> Unit
                }
            }
            .launchIn(lifecycleScope)

        viewModel.isLoading
            .onEach { isLoading ->
                showLoading(isLoading)
            }
            .launchIn(lifecycleScope)
    }

    private fun setupNavigationButtons() {
        binding.prevButton.setOnClickListener {
            if (allPokemonNames.isNotEmpty()) {
                currentIndex = (currentIndex - 1 + allPokemonNames.size) % allPokemonNames.size
                viewModel.load(allPokemonNames[currentIndex])
            } else {
                Toast.makeText(this, "No Pokémon in the list to navigate", Toast.LENGTH_SHORT).show()
            }
        }

        binding.nextButton.setOnClickListener {
            if (allPokemonNames.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % allPokemonNames.size
                viewModel.load(allPokemonNames[currentIndex])
            } else {
                Toast.makeText(this, "No Pokémon in the list to navigate", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupImageToggle() {
        binding.pokemonImage.setOnClickListener {
            if (::currentPokemon.isInitialized) {
                showingShiny = !showingShiny
                val imageUrl = if (showingShiny) currentPokemon.shinyImageUrl else currentPokemon.imageUrl
                val fallbackUrl = if (showingShiny) currentPokemon.officialArtworkUrl else currentPokemon.officialArtworkUrl
                loadGifWithFallback(imageUrl, fallbackUrl)
            }
        }
    }

    private fun updateUI(pokemon: PokemonDetail) {
        showingShiny = false

        binding.pokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
        binding.pokemonNumber.text = "#${pokemon.id.toString().padStart(3, '0')}"
        loadGifWithFallback(pokemon.imageUrl, pokemon.officialArtworkUrl)

        if (pokemon.types.isNotEmpty()) {
            val firstTypeColor = getTypeColor(pokemon.types[0])
            val drawable = binding.typeBackgroundLayout.background?.mutate()
            drawable?.let {
                DrawableCompat.setTint(it, ContextCompat.getColor(this, firstTypeColor))
            }
        } else {
            val drawable = binding.typeBackgroundLayout.background?.mutate()
            drawable?.let {
                DrawableCompat.setTint(it, ContextCompat.getColor(this, android.R.color.transparent))
            }
        }

        if (pokemon.types.isNotEmpty()) {
            binding.pokemontype1.text = pokemon.types[0].replaceFirstChar { it.uppercase() }
            binding.pokemontype1.visibility = View.VISIBLE
            applyTypeBackground(binding.pokemontype1, pokemon.types[0])
        } else {
            binding.pokemontype1.visibility = View.GONE
        }

        if (pokemon.types.size > 1) {
            binding.pokemontype2.text = pokemon.types[1].replaceFirstChar { it.uppercase() }
            binding.pokemontype2.visibility = View.VISIBLE
            applyTypeBackground(binding.pokemontype2, pokemon.types[1])
        } else {
            binding.pokemontype2.visibility = View.GONE
        }

        binding.statHeightValue.text = "${pokemon.height} m"
        binding.statWeightValue.text = "${pokemon.weight} kg"
        binding.statAbilityValue.text = pokemon.ability.replaceFirstChar { it.uppercase() }
        binding.pokemonRegion.text = "Region: ${pokemon.region}"
        binding.pokemonDescription.text = pokemon.description

        if (pokemon.genderRate == -1) {
            binding.pokemonGender.text = "Genderless"
            binding.maleBar.visibility = View.GONE
            binding.femaleBar.visibility = View.GONE
        } else {
            val femalePercent = (pokemon.genderRate / 8.0f) * 100
            val malePercent = 100 - femalePercent
            binding.pokemonGender.text = "${String.format("%.1f", malePercent)}% / ${String.format("%.1f", femalePercent)}%"
            binding.maleBar.visibility = View.VISIBLE
            binding.femaleBar.visibility = View.VISIBLE

            val total = malePercent + femalePercent
            val maleWeight = malePercent / total
            val femaleWeight = femalePercent / total

            (binding.maleBar.layoutParams as? LinearLayout.LayoutParams)?.weight = maleWeight
            (binding.femaleBar.layoutParams as? LinearLayout.LayoutParams)?.weight = femaleWeight

            binding.maleBar.requestLayout()
            binding.femaleBar.requestLayout()
        }
    }

    private fun loadGifWithFallback(url: String, fallbackUrl: String) {
        val request = ImageRequest.Builder(this)
            .data(url)
            .listener(onError = { _, _ ->
                val fallbackRequest = ImageRequest.Builder(this@PokemonDetailActivity)
                    .data(fallbackUrl)
                    .target(binding.pokemonImage)
                    .build()
                imageLoader.enqueue(fallbackRequest)
            })
            .target(binding.pokemonImage)
            .build()

        imageLoader.enqueue(request)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            Glide.with(this).asGif().load(R.drawable.pokeloading).into(binding.loadingGifOverlay)
        }
    }

    private fun getTypeColor(type: String): Int {
        return when (type.trim().lowercase()) {
            "grass" -> R.color.type_grass
            "fire" -> R.color.type_fire
            "water" -> R.color.type_water
            "bug" -> R.color.type_bug
            "normal" -> R.color.type_normal
            "poison" -> R.color.type_poison
            "electric" -> R.color.type_electric
            "ground" -> R.color.type_ground
            "fairy" -> R.color.type_fairy
            "fighting" -> R.color.type_fighting
            "psychic" -> R.color.type_psychic
            "rock" -> R.color.type_rock
            "ghost" -> R.color.type_ghost
            "ice" -> R.color.type_ice
            "dragon" -> R.color.type_dragon
            "dark" -> R.color.type_dark
            "steel" -> R.color.type_steel
            "flying" -> R.color.type_flying
            else -> android.R.color.black
        }
    }

    private fun applyTypeBackground(textView: TextView, type: String) {
        val drawable = ContextCompat.getDrawable(this, R.drawable.bg_type)?.mutate()
        drawable?.let {
            val wrappedDrawable = DrawableCompat.wrap(it)
            DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(this, getTypeColor(type)))
            textView.background = wrappedDrawable
        }
    }
}
