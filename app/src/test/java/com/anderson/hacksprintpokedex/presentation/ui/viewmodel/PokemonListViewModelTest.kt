package com.anderson.hacksprintpokedex.presentation.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anderson.hacksprintpokedex.domain.model.Pokemon
import com.anderson.hacksprintpokedex.domain.usecase.FetchAndStorePokemonUseCase
import com.anderson.hacksprintpokedex.domain.usecase.GetPokemonListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
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
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class PokemonListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var getPokemonListUseCase: GetPokemonListUseCase

    @Mock
    private lateinit var fetchAndStorePokemonUseCase: FetchAndStorePokemonUseCase

    private lateinit var viewModel: PokemonListViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `init should fetch and display pokemon list`() = runBlockingTest {
        val pokemonList = listOf(Pokemon(1, "Bulbasaur", "url", "shiny_url", "artwork_url", listOf("grass")))
        `when`(getPokemonListUseCase()).thenReturn(flowOf(pokemonList))

        viewModel = PokemonListViewModel(getPokemonListUseCase, fetchAndStorePokemonUseCase)

        val successState = viewModel.uiState.first() as PokemonListUiState.Success
        assertEquals(pokemonList, successState.pokemonList)
        verify(fetchAndStorePokemonUseCase).invoke()
    }

    @Test
    fun `fetchPokemonsFromNetwork should handle errors`() = runBlockingTest {
        val errorMessage = "Network error"
        `when`(getPokemonListUseCase()).thenReturn(flowOf(emptyList()))
        `when`(fetchAndStorePokemonUseCase()).thenThrow(RuntimeException(errorMessage))

        viewModel = PokemonListViewModel(getPokemonListUseCase, fetchAndStorePokemonUseCase)

        val errorState = viewModel.uiState.first() as PokemonListUiState.Error
        assertEquals(errorMessage, errorState.message)
    }
}
