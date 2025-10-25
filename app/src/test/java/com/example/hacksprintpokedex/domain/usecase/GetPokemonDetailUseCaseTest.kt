package com.example.hacksprintpokedex.domain.usecase

import com.example.hacksprintpokedex.domain.model.PokemonDetail
import com.example.hacksprintpokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class GetPokemonDetailUseCaseTest {

    @Mock
    private lateinit var pokemonRepository: PokemonRepository

    private lateinit var getPokemonDetailUseCase: GetPokemonDetailUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        getPokemonDetailUseCase = GetPokemonDetailUseCase(pokemonRepository)
    }

    @Test
    fun `invoke should return pokemon detail from repository`() = runBlockingTest {
        val pokemonName = "pikachu"
        val pokemonDetail = PokemonDetail(25, "Pikachu", "url", "shiny_url", "artwork_url", listOf("electric"), 6.0, 0.4, "static", "kanto", "desc", 1)
        `when`(pokemonRepository.getPokemonDetail(pokemonName)).thenReturn(pokemonDetail)

        val result = getPokemonDetailUseCase(pokemonName)

        assertEquals(pokemonDetail, result)
    }
}
