package com.arwin.pokeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arwin.pokeapp.databinding.StatItemPokemonBinding
import com.arwin.pokeapp.model.Stats
import com.arwin.pokeapp.utils.MAX_BASE_STATE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale


class StatsAdapter :
    RecyclerView.Adapter<StatsAdapter.CartViewHolder>() {
    private val stats = ArrayList<Stats>()

    fun setStats(newList: ArrayList<Stats>) {
        stats.clear()
        stats.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {

        return CartViewHolder(
            StatItemPokemonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        holder.bind(stats[position])

    }

    override fun getItemCount(): Int {
        return stats.size
    }

    class CartViewHolder(private val binding: StatItemPokemonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //Max base state is 255, using this set the progressbar progress based on pokemon stat.

        fun bind(stat: Stats) {
            binding.apply {
                val mProgress = progressCircular
                mProgress.secondaryProgress = MAX_BASE_STATE
                mProgress.max = MAX_BASE_STATE

                //The increment animation on progress bar is achieved by this.
                CoroutineScope(Dispatchers.Main).launch {
                    var state = 0
                    while (state <= stat.base_stat) {
                        mProgress.progress = state
                        state++
                        delay(10)
                    }
                }

                statName.text = stat.stat.name.capitalize(Locale.ROOT)

                if (stat.stat.name.contains("-")) {
                    val first = stat.stat.name.substringBefore("-").capitalize(Locale.ROOT)
                    val second = stat.stat.name.substringAfter("-").capitalize(Locale.ROOT)

                    "$first - $second".also { statName.text = it }
                }
                statCount.text = stat.base_stat.toString()

            }
        }
    }
}