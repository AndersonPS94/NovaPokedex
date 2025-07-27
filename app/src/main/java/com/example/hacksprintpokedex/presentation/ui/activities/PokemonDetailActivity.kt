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
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.example.hacksprintpokedex.R
import com.example.hacksprintpokedex.databinding.ActivityPokemonDetailBinding
import com.example.hacksprintpokedex.domain.model.PokemonDetail
import com.example.hacksprintpokedex.presentation.ui.navigation.NavigationTarget
import com.example.hacksprintpokedex.presentation.ui.viewmodel.PokemonDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonDetailBinding
    private val viewModel: PokemonDetailViewModel by viewModels()

    private var allList: List<PokemonDetail> = emptyList()
    private var currentIndex: Int = -1
    private var showingShiny = false
    private lateinit var currentPokemon: PokemonDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        val pokemonList = intent.getParcelableArrayListExtra<PokemonDetail>("pokemonList")
        val pokemonName = intent.getStringExtra("pokemonName")

        if (pokemonList != null && pokemonName != null) {
            allList = pokemonList
            currentIndex = allList.indexOfFirst { it.name.equals(pokemonName, ignoreCase = true) }

            if (currentIndex == -1) {
                Toast.makeText(this, "Pokémon não encontrado na lista", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
            updateUI(allList[currentIndex])
        } else if (pokemonName != null) {
            viewModel.load(pokemonName)
        } else {
            Toast.makeText(this, "Dados insuficientes para exibir Pokémon", Toast.LENGTH_SHORT)
                .show()
            finish()
            return
        }

        observeViewModel()
        setupNavigationButtons()
        setupImageToggle()
    }

    private fun observeViewModel() {
        viewModel.pokemonDetail.observe(this) { pokemon ->
            pokemon?.let { updateUI(it) }
        }

        viewModel.navigationTarget.observe(this) { event ->
            event.getContentIfNotHandled()?.let { target ->
                when (target) {
                    is NavigationTarget.ErrorActivity -> {
                        val intent = Intent(this, ErrorActivity::class.java).apply {
                            putExtra("errorMessage", "Erro ao carregar dados do Pokémon.")
                        }
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun setupNavigationButtons() {

        binding.lottieAnim.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        binding.prevButton.setOnClickListener {
            if (allList.isNotEmpty()) {
                currentIndex = (currentIndex - 1 + allList.size) % allList.size
                updateUI(allList[currentIndex])
            } else {
                Toast.makeText(this, "Nenhum Pokémon na lista para navegar", Toast.LENGTH_SHORT).show()
            }
        }

        binding.nextButton.setOnClickListener {
            if (allList.isNotEmpty()) {
                currentIndex = (currentIndex + 1) % allList.size
                updateUI(allList[currentIndex])
            } else {
                Toast.makeText(this, "Nenhum Pokémon na lista para navegar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupImageToggle() {
        binding.pokemonImage.setOnClickListener {
            showingShiny = !showingShiny
            val imageUrl = if (showingShiny) currentPokemon.shinyImageUrl else currentPokemon.imageUrl
            loadGif(imageUrl)
        }
    }

    private fun updateUI(pokemon: PokemonDetail) {
        currentPokemon = pokemon
        showingShiny = false

        binding.pokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
        binding.pokemonNumber.text = "#${pokemon.id.toString().padStart(3, '0')}"
        loadGif(pokemon.imageUrl)

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

    private fun loadGif(url: String) {
        val imageLoader = ImageLoader.Builder(this)
            .components {
                if (android.os.Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        val request = ImageRequest.Builder(this)
            .data(url)
            .target(binding.pokemonImage)
            .build()

        imageLoader.enqueue(request)
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
