package com.example.hacksprintpokedex.presentation.ui.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hacksprintpokedex.R
import com.example.hacksprintpokedex.databinding.ActivityMainBinding
import com.example.hacksprintpokedex.domain.model.PokemonDetail
import com.example.hacksprintpokedex.presentation.ui.adapters.PokemonAdapter
import com.example.hacksprintpokedex.presentation.ui.viewmodel.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var adapter: PokemonAdapter
    private lateinit var splashMediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()

        splashMediaPlayer = MediaPlayer.create(this, R.raw.pikapipikachu)
        splashMediaPlayer.start()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        splash.setKeepOnScreenCondition {
            (viewModel.isLoading.value ?: true) || splashMediaPlayer.isPlaying
        }

        adapter = PokemonAdapter(emptyList()) { pokemon ->
            if (pokemon.name.lowercase() == "pikachu") {
                val pikachuSound = MediaPlayer.create(this, R.raw.pikachu)
                pikachuSound.setOnCompletionListener {
                    pikachuSound.release()
                    goToDetail(pokemon)
                }
                pikachuSound.start()
            } else {
                goToDetail(pokemon)
            }
        }

        binding.pokemonList.layoutManager = LinearLayoutManager(this)
        binding.pokemonList.adapter = adapter

        viewModel.pokemonList.observe(this) { list ->
            adapter.updateList(list)
        }

        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?) = false
            override fun onQueryTextChange(q: String?): Boolean {
                adapter.filterList(q.orEmpty())
                return true
            }
        })

        viewModel.loadPokemons()
    }

    private fun goToDetail(pokemon: PokemonDetail) {
        val intent = Intent(this, PokemonDetailActivity::class.java).apply {
            putParcelableArrayListExtra("pokemonList", ArrayList(viewModel.pokemonList.value ?: emptyList()))
            putExtra("pokemonName", pokemon.name)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        splashMediaPlayer.release()
    }
}
