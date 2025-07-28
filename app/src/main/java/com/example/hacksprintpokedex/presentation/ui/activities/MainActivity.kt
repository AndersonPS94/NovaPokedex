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

    // Armazena a lista atual do estado da UI para uso posterior.
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

        // `viewModel.loadPokemons()` foi removido, pois a lógica de busca inicial
        // e armazenamento já está no bloco `init` do ViewModel, garantindo que
        // os dados sejam carregados automaticamente quando o ViewModel é criado.
    }

    /**
     * Configura a splash screen e inicia a reprodução do som.
     * A splash permanece visível enquanto os dados estão carregando ou o som está tocando.
     */
    private fun initSplashScreen(splash: androidx.core.splashscreen.SplashScreen) {
        splashMediaPlayer = MediaPlayer.create(this, R.raw.pikapipikachu)
        splashMediaPlayer.start()

        splash.setKeepOnScreenCondition {
            // A condição agora verifica o estado atual do `uiState` do ViewModel
            // para determinar se os dados ainda estão sendo carregados,
            // ou se o som da splash screen ainda está tocando.
            val isDataLoading = viewModel.uiState.value is PokemonListUiState.Loading
            isDataLoading || splashMediaPlayer.isPlaying
        }
    }

    /**
     * Carrega os GIFs animados nos seus respectivos `ImageViews`.
     * A visibilidade desses GIFs será gerenciada pela função `observePokemonListUiState`.
     */
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

        // Inicialmente, oculta todos os GIFs de status.
        // O `observePokemonListUiState` vai definir a visibilidade correta com base no estado dos dados.
        binding.pokeLoading.visibility = View.GONE
        binding.pokeFail.visibility = View.GONE
        binding.pokeSucess.visibility = View.GONE
        binding.pikachuERed.visibility = View.GONE // Oculta o GIF principal da splash também

        // Configura o listener de clique para o GIF principal do Pikachu.
        binding.pikachuERed.setOnClickListener {
            if (!pikachuGotchaSound.isPlaying) {
                pikachuGotchaSound.start()
            }
        }
    }

    /**
     * Configura o RecyclerView e seu Adapter.
     * O clique em um item agora passa o objeto `Pokemon` (modelo de lista) para o handler.
     */
    private fun initRecyclerView() {
        // O adapter agora espera `List<Pokemon>` (o modelo de domínio unificado).
        adapter = PokemonAdapter(emptyList()) { pokemon ->
            // Ao clicar em um Pokémon da lista, decide a navegação ou ação.
            if (pokemon.name.lowercase() == "pikachu") {
                playPikachuSoundThenNavigate(pokemon)
            } else {
                goToDetail(pokemon.name)
            }
        }
        binding.pokemonList.layoutManager = LinearLayoutManager(this)
        binding.pokemonList.adapter = adapter
    }

    /**
     * Observa o `uiState` do ViewModel, que é um `StateFlow`, e atualiza a interface do usuário
     * de acordo com os estados `Loading`, `Success` e `Error`.
     */
    private fun observePokemonListUiState() {
        lifecycleScope.launch {
            // `repeatOnLifecycle` garante que a coleta do Flow ocorra apenas quando a Activity
            // está no estado STARTED ou superior, evitando vazamentos de memória e atualizações desnecessárias.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState ->
                    // Antes de definir a visibilidade para o estado atual,
                    // esconde todos os GIFs de status para evitar que múltiplos fiquem visíveis simultaneamente.
                    binding.pokeLoading.visibility = View.GONE
                    binding.pokeFail.visibility = View.GONE
                    binding.pokeSucess.visibility = View.GONE
                    binding.pikachuERed.visibility = View.VISIBLE

                    when (uiState) {
                        is PokemonListUiState.Loading -> {
                            // Quando os dados estão carregando, exibe o GIF de carregamento
                            // e oculta a lista do RecyclerView.
                            binding.pokeLoading.visibility = View.VISIBLE
                            binding.pokemonList.visibility = View.GONE
                            Log.d("MainActivity", "Loading data...")
                        }
                        is PokemonListUiState.Success -> {
                            // Quando os dados são carregados com sucesso, esconde os GIFs de status
                            // (já feito no início do 'when') e exibe a lista do RecyclerView.
                            binding.pokemonList.visibility = View.VISIBLE
                            adapter.updateList(uiState.pokemonList)
                            currentPokemonList = uiState.pokemonList // Armazena a lista atual
                            Log.d("MainActivity", "Data loaded: ${uiState.pokemonList.size} pokemons")

                            // Opcional: Se desejar mostrar o GIF de sucesso por um breve momento,
                            // descomente o bloco abaixo. Ele aparecerá e depois sumirá automaticamente.
                            /*
                            binding.pokeSucess.visibility = View.VISIBLE
                            lifecycleScope.launch {
                                delay(1500) // Mostra o GIF de sucesso por 1.5 segundos
                                binding.pokeSucess.visibility = View.GONE
                            }
                            */
                        }
                        is PokemonListUiState.Error -> {
                            // Em caso de erro, esconde os GIFs de status (já feito no início do 'when'),
                            // exibe o GIF de falha e mostra uma mensagem Toast de erro.
                            // A lista do RecyclerView permanece visível se houver dados antigos.
                            binding.pokeFail.visibility = View.VISIBLE // Mostra o GIF de falha
                            binding.pokemonList.visibility = View.VISIBLE // Mantém a lista visível (se houver dados)
                            Toast.makeText(this@MainActivity, "Erro: ${uiState.message}", Toast.LENGTH_LONG).show()
                            Log.e("MainActivity", "Erro ao carregar dados: ${uiState.message}")
                        }
                    }
                }
            }
        }
    }

    /**
     * Configura a barra de pesquisa para filtrar a lista de Pokémons exibida no RecyclerView.
     */
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(q: String?) = false // Não faz nada ao submeter a pesquisa
            override fun onQueryTextChange(q: String?): Boolean {
                adapter.filterList(q.orEmpty()) // Filtra a lista do adapter com o texto digitado
                return true
            }
        })
    }

    /**
     * Toca um som específico do Pikachu antes de navegar para a tela de detalhes.
     * Esta função agora recebe o modelo unificado `Pokemon` para acesso ao nome.
     */
    private fun playPikachuSoundThenNavigate(pokemon: Pokemon) {
        val pikachuSound = MediaPlayer.create(this, R.raw.pikachu)
        pikachuSound.setOnCompletionListener {
            pikachuSound.release() // Libera o recurso do MediaPlayer
            goToDetail(pokemon.name) // Navega para a tela de detalhes com o nome do Pokémon
        }
        pikachuSound.start()
    }

    /**
     * Navega para a `PokemonDetailActivity`.
     * Recebe apenas o nome do Pokémon, que será usado pela `PokemonDetailActivity`
     * para buscar os detalhes completos. A lista de nomes também é passada para permitir a navegação entre Pokémons.
     */
    private fun goToDetail(pokemonName: String) {
        val intent = Intent(this, PokemonDetailActivity::class.java).apply {
            // Mapeia a lista de objetos Pokemon para uma lista de strings contendo apenas os nomes.
            // Isso é essencial para que a PokemonDetailActivity possa navegar entre os Pokémons.
            val allNames = ArrayList(currentPokemonList.map { it.name })
            putStringArrayListExtra("pokemonNamesList", allNames)

            // Passa o nome do Pokémon clicado, que será o Pokémon inicial na tela de detalhes.
            putExtra("pokemonName", pokemonName)
        }
        startActivity(intent)
    }

    /**
     * Libera o `MediaPlayer` da splash screen quando a Activity é destruída para evitar vazamentos de memória.
     */
    override fun onDestroy() {
        super.onDestroy()
        splashMediaPlayer.release()
    }
}