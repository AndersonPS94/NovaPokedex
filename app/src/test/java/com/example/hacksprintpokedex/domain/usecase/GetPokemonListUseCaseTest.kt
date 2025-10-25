package com.example.hacksprintpokedex.domain.usecase

import com.example.hacksprintpokedex.domain.model.Pokemon
import com.example.hacksprintpokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class GetPokemonListUseCaseTest {

    @Mock
    private lateinit var pokemonRepository: PokemonRepository

    private lateinit var getPokemonListUseCase: GetPokemonListUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getPokemonListUseCase = GetPokemonListUseCase(pokemonRepository)
    }

    @Test
    fun `invoke should return pokemon list from repository`() = runBlockingTest {
        val pokemonList = listOf(Pokemon(1, "Bulbasaur", "url", "shiny_url", "artwork_url", listOf("grass")))
        `when`(pokemonRepository.getPokemonList()).thenReturn(flowOf(pokemonList))

        val result = getPokemonListUseCase().first()

        assertEquals(pokemonList, result)
    }
}
