package com.anderson.hacksprintpokedex.data.di

import com.anderson.hacksprintpokedex.data.local.PokemonDao
import com.anderson.hacksprintpokedex.data.local.PokemonDetailDao
import com.anderson.hacksprintpokedex.data.remote.api.PokeApiService
import com.anderson.hacksprintpokedex.data.repository.PokemonRepositoryImpl
import com.anderson.hacksprintpokedex.domain.repository.PokemonRepository
import com.anderson.hacksprintpokedex.domain.usecase.GetPokemonDetailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePokeApiService(): PokeApiService {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonRepository(
        apiService: PokeApiService,
        pokemonDao: PokemonDao,
        pokemonDetailDao: PokemonDetailDao
    ): PokemonRepository {
        return PokemonRepositoryImpl(apiService, pokemonDao, pokemonDetailDao)
    }

    @Provides
    fun provideGetPokemonDetailUseCase(repo: PokemonRepository): GetPokemonDetailUseCase {
        return GetPokemonDetailUseCase(repo)
    }
}
