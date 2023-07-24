package com.arwin.pokeapp.fragments

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.arwin.pokeapp.R
import com.arwin.pokeapp.adapters.StatsAdapter
import com.arwin.pokeapp.databinding.FragmentPokemonStatsBinding
import com.arwin.pokeapp.model.PokemonResult
import com.arwin.pokeapp.model.Stats
import com.arwin.pokeapp.utils.NetworkResource
import com.arwin.pokeapp.utils.toast
import com.arwin.pokeapp.viewmodels.PokemonStatsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale

@AndroidEntryPoint
class PokemonStatsFragment : Fragment(R.layout.fragment_pokemon_stats) {

    private lateinit var binding: FragmentPokemonStatsBinding
    private val adapter = StatsAdapter()
    private val args = PokemonStatsFragmentArgs
    private val viewModel: PokemonStatsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPokemonStatsBinding.bind(view)
        val argument = arguments?.let { args.fromBundle(it) }
        val pokemonResult = argument?.pokemonResult
        val dominantColor = argument?.dominantColor
        val picture = argument?.picture

        //setting the colors based on dominant colors
        if (dominantColor != 0) {
            dominantColor?.let { theColor ->
                binding.card.setBackgroundColor(theColor)
                binding.toolbar.setBackgroundColor(theColor)
                requireActivity().window.statusBarColor = theColor
            }
        }

        val toolbar = binding.toolbar
        toolbar.elevation = 0.0F
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.title = pokemonResult?.name?.capitalize(
            Locale.getDefault())
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(true)

        toolbar.setNavigationOnClickListener {
            binding.root.findNavController().navigateUp()
        }

        //load pic
        binding.apply {
            Glide.with(root)
                .load(picture)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(pokemonItemImage)
        }

        pokemonResult?.let { loadSinglePokemon(it) }

    }


    private fun loadSinglePokemon(pokemonResult: PokemonResult) {

        lifecycleScope.launch(Dispatchers.Main) {
            //a bit delay for the animation to finish
            delay(300)
            viewModel.getSinglePokemon(pokemonResult.url).collect {
                when (it) {
                    is NetworkResource.Success -> {
                        binding.progressCircular.isVisible = false
                        binding.apply {
                            (it.value.weight.div(10.0).toString() + " kgs").also { weight ->
                                pokemonItemWeight.text = weight
                            }
                            (it.value.height.div(10.0).toString() + " metres").also { height ->
                                pokemonItemHeight.text = height
                            }
                            pokemonStatList.adapter = adapter
                            adapter.setStats(it.value.stats as ArrayList<Stats>)
                        }
                    }
                    is NetworkResource.Failure -> {
                        binding.progressCircular.isVisible = false
                        requireContext().toast("There was an error loading the pokemon")
                    }
                    is NetworkResource.Loading -> {
                        binding.progressCircular.isVisible = true
                    }
                }
            }
        }
    }
}