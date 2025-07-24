package com.example.hacksprintpokedex.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hacksprintpokedex.R
import com.example.hacksprintpokedex.databinding.ActivityMainBinding
import com.example.hacksprintpokedex.presentation.ui.adapters.PokemonAdapter
import com.example.hacksprintpokedex.presentation.ui.viewmodel.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var adapter: PokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            viewModel.isLoading.value ?: true
        }

        setupRecyclerView()
        setupObservers()

        viewModel.loadPokemons()
    }

    private fun setupRecyclerView() {
        adapter = PokemonAdapter(emptyList()) { pokemon ->
            val intent = Intent(this, PokemonDetailActivity::class.java).apply {
                putExtra("pokemon_id", pokemon.id)
                putExtra("pokemon_name", pokemon.name)
                putExtra("pokemon_types", pokemon.types.toTypedArray())
            }
            startActivity(intent)
        }
        binding.pokemonList.adapter = adapter
        binding.pokemonList.layoutManager = LinearLayoutManager(this)
    }

    private fun setupObservers() {
        viewModel.pokemonList.observe(this) { list ->
            adapter.updateList(list)
        }
    }
}
