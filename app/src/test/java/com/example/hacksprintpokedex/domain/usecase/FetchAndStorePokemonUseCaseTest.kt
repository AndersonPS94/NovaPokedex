package com.example.hacksprintpokedex.domain.usecase

import com.example.hacksprintpokedex.domain.repository.PokemonRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class FetchAndStorePokemonUseCaseTest {

    @Mock
    private lateinit var pokemonRepository: PokemonRepository

    private lateinit var fetchAndStorePokemonUseCase: FetchAndStorePokemonUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        fetchAndStorePokemonUseCase = FetchAndStorePokemonUseCase(pokemonRepository)
    }

    @Test
    fun `invoke should call fetchAndStorePokemonList on repository`() = runBlockingTest {
        fetchAndStorePokemonUseCase()

        verify(pokemonRepository).fetchAndStorePokemonList()
    }
}
