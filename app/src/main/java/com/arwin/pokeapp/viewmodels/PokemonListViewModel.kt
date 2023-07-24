package com.arwin.pokeapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arwin.pokeapp.data.repositories.PokemonRepository
import com.arwin.pokeapp.model.PokemonResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonRepository: PokemonRepository
) :
    ViewModel() {

    private var currentResult: Flow<PagingData<PokemonResult>>? = null
    fun getPokemons(searchString: String?): Flow<PagingData<PokemonResult>> {
        val newResult: Flow<PagingData<PokemonResult>> =
            pokemonRepository.getPokemon(searchString).cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }
}