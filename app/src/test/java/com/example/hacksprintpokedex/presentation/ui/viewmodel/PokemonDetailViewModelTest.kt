package com.example.hacksprintpokedex.presentation.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.hacksprintpokedex.domain.model.PokemonDetail
import com.example.hacksprintpokedex.domain.usecase.GetPokemonDetailUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class PokemonDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var getPokemonDetailUseCase: GetPokemonDetailUseCase

    private lateinit var viewModel: PokemonDetailViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = PokemonDetailViewModel(getPokemonDetailUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `load should update uiState to Success when use case is successful`() = runBlockingTest {
        val pokemonName = "pikachu"
        val pokemonDetail = PokemonDetail(25, "Pikachu", "url", "shiny_url", "artwork_url", listOf("electric"), 6.0, 0.4, "static", "kanto", "desc", 1)
        `when`(getPokemonDetailUseCase(pokemonName)).thenReturn(pokemonDetail)

        viewModel.load(pokemonName)

        val successState = viewModel.uiState.first() as PokemonDetailUiState.Success
        assertEquals(pokemonDetail, successState.pokemonDetail)
    }

    @Test
    fun `load should update uiState to Error when use case throws exception`() = runBlockingTest {
        val pokemonName = "pikachu"
        val errorMessage = "An error occurred"
        `when`(getPokemonDetailUseCase(pokemonName)).thenThrow(RuntimeException(errorMessage))

        viewModel.load(pokemonName)

        val errorState = viewModel.uiState.first() as PokemonDetailUiState.Error
        assertEquals(errorMessage, errorState.message)
    }
}
