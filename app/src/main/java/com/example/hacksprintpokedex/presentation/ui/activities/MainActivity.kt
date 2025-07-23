package com.example.hacksprintpokedex.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hacksprintpokedex.databinding.ActivityMainBinding
import com.example.hacksprintpokedex.data.model.Pokemon
import com.example.hacksprintpokedex.data.remote.api.PokeApiService
import com.example.hacksprintpokedex.data.model.PokemonDetailResponse
import com.example.hacksprintpokedex.presentation.ui.adapters.PokemonAdapter
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val pokemonList = mutableListOf<Pokemon>()
    private lateinit var adapter: PokemonAdapter

    private val apiService: PokeApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        Thread.sleep(2000)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inicializarRecyclerView()
        carregarPokemons()
    }

    private fun inicializarRecyclerView() {
        adapter = PokemonAdapter(pokemonList) { pokemon ->
            val intent = Intent(this, PokemonDetailActivity::class.java).apply {
                putExtra("pokemon_id", pokemon.id)
                putExtra("pokemon_name", pokemon.name)
                putExtra("pokemon_types", pokemon.types.toTypedArray())
            }
            startActivity(intent)
        }
        binding.pokemonList.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }
    }

    private fun carregarPokemons() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getPokemonList(limit = 151)
                if (response.isSuccessful) {
                    val listaBasica = response.body()?.results ?: emptyList()

                    for (pokemonBasico in listaBasica) {
                        val detalheResponse = apiService.getPokemon(pokemonBasico.name)
                        if (detalheResponse.isSuccessful) {
                            val detalhe: PokemonDetailResponse? = detalheResponse.body()
                            detalhe?.let {
                                val tipos = it.types.sortedBy { slot -> slot.slot }.map { slot -> slot.type.name }
                                val pokemon = Pokemon(it.id, it.name, tipos)
                                pokemonList.add(pokemon)

                                withContext(Dispatchers.Main) {
                                    adapter.notifyItemInserted(pokemonList.size - 1)
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Erro ao carregar Pokémons", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
