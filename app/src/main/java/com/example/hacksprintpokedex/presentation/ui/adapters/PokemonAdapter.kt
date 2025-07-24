package com.example.hacksprintpokedex.presentation.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hacksprintpokedex.R
import com.example.hacksprintpokedex.databinding.ItemPokemonRvBinding
import com.example.hacksprintpokedex.data.model.Pokemon
import com.squareup.picasso.Picasso

class PokemonAdapter(
    private var items: List<Pokemon>,
    private val onItemClick: (Pokemon) -> Unit
) : RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    class PokemonViewHolder(val binding: ItemPokemonRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemPokemonRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = items[position]
        with(holder.binding) {
            tvPokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
            tvPokemonNumber.text = "#${pokemon.id.toString().padStart(3, '0')}"
            val imageUrl =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png"
            Picasso.get().load(imageUrl).into(imagePokemon)

            layoutTypes.removeAllViews()
            for (type in pokemon.types) {
                val typeView = TextView(root.context).apply {
                    text = type.replaceFirstChar { it.uppercase() }
                    setTextColor(Color.WHITE)
                    setPadding(24, 12, 24, 12)
                    setBackgroundResource(R.drawable.bg_type)
                    backgroundTintList = ColorStateList.valueOf(getColorByType(type))
                }
                layoutTypes.addView(typeView)
            }

            root.setOnClickListener {
                onItemClick(pokemon)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newList: List<Pokemon>) {
        items = newList
        notifyDataSetChanged()
    }

    private fun getColorByType(type: String): Int {
        return when (type.lowercase()) {
            "normal" -> 0xFFA8A77A.toInt()
            "fire" -> 0xFFEE8130.toInt()
            "water" -> 0xFF6390F0.toInt()
            "electric" -> 0xFFF7D02C.toInt()
            "grass" -> 0xFF7AC74C.toInt()
            "ice" -> 0xFF96D9D6.toInt()
            "fighting" -> 0xFFC22E28.toInt()
            "poison" -> 0xFFA33EA1.toInt()
            "ground" -> 0xFFE2BF65.toInt()
            "flying" -> 0xFFA98FF3.toInt()
            "psychic" -> 0xFFF95587.toInt()
            "bug" -> 0xFFA6B91A.toInt()
            "rock" -> 0xFFB6A136.toInt()
            "ghost" -> 0xFF735797.toInt()
            "dragon" -> 0xFF6F35FC.toInt()
            "dark" -> 0xFF705746.toInt()
            "steel" -> 0xFFB7B7CE.toInt()
            "fairy" -> 0xFFD685AD.toInt()
            else -> 0xFFBDBDBD.toInt()
        }
    }
}
