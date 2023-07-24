package com.arwin.pokeapp.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.arwin.pokeapp.api.PokemonApi
import com.arwin.pokeapp.data.datasource.PokemonDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository @Inject constructor(private val pokemonApi: PokemonApi) : BaseRepository() {

    //Returning the fetched data as flow

    fun getPokemon(searchString: String?) = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = 25),
        pagingSourceFactory = {
            PokemonDataSource(pokemonApi, searchString)
        }
    ).flow

    suspend fun getSinglePokemon(id: Int) = safeApiCall {
        pokemonApi.getSinglePokemon(id)

    }


}