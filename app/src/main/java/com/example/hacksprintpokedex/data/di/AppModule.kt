package com.example.hacksprintpokedex.data.di

import com.example.hacksprintpokedex.data.local.PokemonDao // <--- NOVO IMPORT
import com.example.hacksprintpokedex.data.remote.api.PokeApiService
import com.example.hacksprintpokedex.data.repository.PokemonRepositoryImpl
import com.example.hacksprintpokedex.domain.repository.PokemonRepository
import com.example.hacksprintpokedex.domain.usecase.GetPokemonDetailUseCase
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
        pokemonDao: PokemonDao // <--- AGORA INJETA O PokemonDao AQUI
    ): PokemonRepository {
        return PokemonRepositoryImpl(apiService, pokemonDao) // <--- E PASSA ELE PARA O CONSTRUTOR
    }

    @Provides
    fun provideGetPokemonDetailUseCase(repo: PokemonRepository): GetPokemonDetailUseCase {
        return GetPokemonDetailUseCase(repo)
    }
}