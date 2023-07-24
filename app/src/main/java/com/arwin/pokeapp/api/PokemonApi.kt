package com.arwin.pokeapp.api

import com.arwin.pokeapp.model.PokemonResponse
import com.arwin.pokeapp.model.SinglePokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PokemonApi {
    @GET("pokemon/")
    suspend fun getPokemons(
        @Query("limit") limit: Int?,
        @Query("offset") offset: Int?
    ): PokemonResponse

    @GET("pokemon/{id}/")
    suspend fun getSinglePokemon(
        @Path("id") id: Int
    ): SinglePokemonResponse
}