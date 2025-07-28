package com.example.hacksprintpokedex.presentation.ui.activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        initSplashScreen(splash)
        initGifAnimation()
        initRecyclerView()
        observePokemonList()
        setupSearchView()

        viewModel.loadPokemons()
    }

    /** Configura a splash e toca o som */
    private fun initSplashScreen(splash: androidx.core.splashscreen.SplashScreen) {
        splashMediaPlayer = MediaPlayer.create(this, R.raw.pikapipikachu)
        splashMediaPlayer.start()

        splash.setKeepOnScreenCondition {
            (viewModel.isLoading.value ?: true) || splashMediaPlayer.isPlaying
        }
    }

    /** Exibe o GIF animado */
    private fun initGifAnimation() {
        val pikachuGotchaSound = MediaPlayer.create(this, R.raw.aaaepikachu)
        Glide.with(this)
            .asGif()
            .load(R.drawable.pikachuered)
            .into(binding.pikachuERed)

        binding.pikachuERed.setOnClickListener {
            if(!pikachuGotchaSound.isPlaying) {
                pikachuGotchaSound.start()

            }
        }
    }

    /** Configura RecyclerView e Adapter */
    private fun initRecyclerView() {
        adapter = PokemonAdapter(emptyList()) { pokemon ->
            if (pokemon.name.lowercase() == "pikachu") {
                playPikachuSoundThenNavigate(pokemon)
            } else {
                goToDetail(pokemon)
            }
        }
        binding.pokemonList.layoutManager = LinearLayoutManager(this)
        binding.pokemonList.adapter = adapter
    }

    /** Observa a lista de pokémons e atualiza o adapter */
    private fun observePokemonList() {
        viewModel.pokemonList.observe(this) { list ->
            adapter.updateList(list)
        }
    }

    /** Configura a SearchView para filtrar pokémons */
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?) = false
            override fun onQueryTextChange(q: String?): Boolean {
                adapter.filterList(q.orEmpty())
                return true
            }
        })
    }

    /** Toca som do Pikachu antes de ir para a tela de detalhes */
    private fun playPikachuSoundThenNavigate(pokemon: PokemonDetail) {
        val pikachuSound = MediaPlayer.create(this, R.raw.pikachu)
        pikachuSound.setOnCompletionListener {
            pikachuSound.release()
            goToDetail(pokemon)
        }
        pikachuSound.start()
    }

    /** Navega para a tela de detalhes */
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
