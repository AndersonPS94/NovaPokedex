package com.example.hacksprintpokedex.presentation.ui.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View // Importe View explicitamente
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.hacksprintpokedex.R
import com.example.hacksprintpokedex.databinding.ActivityMainBinding
import com.example.hacksprintpokedex.domain.model.Pokemon
import com.example.hacksprintpokedex.presentation.ui.adapters.PokemonAdapter
import com.example.hacksprintpokedex.presentation.ui.viewmodel.PokemonListUiState
import com.example.hacksprintpokedex.presentation.ui.viewmodel.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: PokemonListViewModel by viewModels()
    private lateinit var adapter: PokemonAdapter
    private lateinit var splashMediaPlayer: MediaPlayer

    //
    private var currentPokemonList: List<Pokemon> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        initSplashScreen(splash)
        initGifAnimation()
        initRecyclerView()
        observePokemonListUiState()
        setupSearchView()

    }



    private fun initSplashScreen(splash: androidx.core.splashscreen.SplashScreen) {
        splashMediaPlayer = MediaPlayer.create(this, R.raw.pikapipikachu)
        splashMediaPlayer.start()

        splash.setKeepOnScreenCondition {

            val isDataLoading = viewModel.uiState.value is PokemonListUiState.Loading
            isDataLoading || splashMediaPlayer.isPlaying
        }
    }


    private fun initGifAnimation() {
        val pikachuGotchaSound = MediaPlayer.create(this, R.raw.aaaepikachu)
        Glide.with(this)
            .asGif()
            .load(R.drawable.pikachuered)
            .into(binding.pikachuERed)

        Glide.with(this)
            .asGif()
            .load(R.drawable.pokeloading)
            .into(binding.pokeLoading)

        Glide.with(this)
            .asGif()
            .load(R.drawable.pokefail)
            .into(binding.pokeFail)

        Glide.with(this)
            .asGif()
            .load(R.drawable.pokesucess)
            .into(binding.pokeSucess)

        binding.pokeLoading.visibility = View.GONE
        binding.pokeFail.visibility = View.GONE
        binding.pokeSucess.visibility = View.GONE
        binding.pikachuERed.visibility = View.GONE


        binding.pikachuERed.setOnClickListener {
            if (!pikachuGotchaSound.isPlaying) {
                pikachuGotchaSound.start()
            }
        }
    }


    private fun initRecyclerView() {
        adapter = PokemonAdapter(emptyList()) { pokemon ->
            if (pokemon.name.lowercase() == "pikachu") {
                playPikachuSoundThenNavigate(pokemon)
            } else {
                goToDetail(pokemon.name)
            }
        }
        binding.pokemonList.layoutManager = LinearLayoutManager(this)
        binding.pokemonList.adapter = adapter
    }


    private fun observePokemonListUiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState ->

                    binding.pokeLoading.visibility = View.GONE
                    binding.pokeFail.visibility = View.GONE
                    binding.pokeSucess.visibility = View.GONE
                    binding.pikachuERed.visibility = View.VISIBLE

                    when (uiState) {
                        is PokemonListUiState.Loading -> {
                            binding.pokeLoading.visibility = View.VISIBLE
                            binding.pokemonList.visibility = View.GONE
                            Log.d("MainActivity", "Loading data...")
                        }
                        is PokemonListUiState.Success -> {
                            binding.pokemonList.visibility = View.VISIBLE
                            adapter.updateList(uiState.pokemonList)
                            currentPokemonList = uiState.pokemonList // Armazena a lista atual
                            Log.d("MainActivity", "Data loaded: ${uiState.pokemonList.size} pokemons")


                        }
                        is PokemonListUiState.Error -> {
                            binding.pokeFail.visibility = View.VISIBLE
                            binding.pokemonList.visibility = View.VISIBLE
                            Toast.makeText(this@MainActivity, "Erro: ${uiState.message}", Toast.LENGTH_LONG).show()
                            Log.e("MainActivity", "Erro ao carregar dados: ${uiState.message}")
                        }
                    }
                }
            }
        }
    }


    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?) = false
            override fun onQueryTextChange(q: String?): Boolean {
                adapter.filterList(q.orEmpty())
                return true
            }
        })
    }


    private fun playPikachuSoundThenNavigate(pokemon: Pokemon) {
        val pikachuSound = MediaPlayer.create(this, R.raw.pikachu)
        pikachuSound.setOnCompletionListener {
            pikachuSound.release()
            goToDetail(pokemon.name)
        }
        pikachuSound.start()
    }


    private fun goToDetail(pokemonName: String) {
        val intent = Intent(this, PokemonDetailActivity::class.java).apply {
            val allNames = ArrayList(currentPokemonList.map { it.name })
            putStringArrayListExtra("pokemonNamesList", allNames)

            putExtra("pokemonName", pokemonName)
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        splashMediaPlayer.release()
    }
}