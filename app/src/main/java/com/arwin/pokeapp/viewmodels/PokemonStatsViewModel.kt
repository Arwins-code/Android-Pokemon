package com.arwin.pokeapp.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.arwin.pokeapp.data.repositories.PokemonRepository
import com.arwin.pokeapp.utils.NetworkResource
import com.arwin.pokeapp.utils.extractId
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class PokemonStatsViewModel @Inject constructor(private val pokemonRepository: PokemonRepository) :
    ViewModel() {

    suspend fun getSinglePokemon(url: String) = flow {
        val id = url.extractId()
        emit(NetworkResource.Loading)
        emit(pokemonRepository.getSinglePokemon(id))
    }

}