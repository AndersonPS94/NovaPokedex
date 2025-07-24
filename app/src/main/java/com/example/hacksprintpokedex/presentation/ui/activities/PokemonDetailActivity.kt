package com.example.hacksprintpokedex.presentation.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hacksprintpokedex.databinding.ActivityPokemonDetailBinding
import com.example.hacksprintpokedex.presentation.ui.navigation.NavigationTarget
import com.example.hacksprintpokedex.presentation.ui.viewmodel.PokemonDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PokemonDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonDetailBinding
    private val viewModel: PokemonDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeViewModel()

        val name = intent.getStringExtra("pokemonName") ?: ""
        viewModel.loadPokemon(name)
    }

    private fun observeViewModel() {
        viewModel.navigateEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let { target ->
                when (target) {
                    is NavigationTarget.ErrorActivity -> {
                        val intent = Intent(this, ErrorActivity::class.java)
                        startActivity(intent)

                    }
                }
            }
        }
    }
}
