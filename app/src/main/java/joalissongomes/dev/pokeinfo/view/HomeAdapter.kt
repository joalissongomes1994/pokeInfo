package joalissongomes.dev.pokeinfo.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import joalissongomes.dev.pokeinfo.R
import joalissongomes.dev.pokeinfo.model.PokemonDetail
import joalissongomes.dev.pokeinfo.utils.getColor
import joalissongomes.dev.pokeinfo.utils.loadingCardWithImage
import joalissongomes.dev.pokeinfo.utils.loadingRecyclerViewPokemonType
import joalissongomes.dev.pokeinfo.utils.navigateToPokemonDetailsFragment


class HomeAdapter(
    private val navController: NavController,
    private val pokemonList: List<PokemonDetail>
) :
    RecyclerView.Adapter<HomeAdapter.MainViewAdapter>() {

    private lateinit var rvPokemonType: RecyclerView
    private lateinit var adapter: PokemonTypeAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewAdapter {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return MainViewAdapter(view)
    }

    override fun onBindViewHolder(holder: MainViewAdapter, position: Int) {
        val itemCurrent = pokemonList[position]

        val params = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )

        if (position == 0) params.topMargin = 40

        params.marginStart = 24
        params.marginEnd = 24

        holder.itemView.layoutParams = params
        holder.bind(itemCurrent)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    inner class MainViewAdapter(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: PokemonDetail) {

            val txtName = itemView.findViewById<TextView>(R.id.txt_pokemon_name)
            txtName.text = item.name.lowercase().replaceFirstChar { it.uppercase() }

            val txtOrder = itemView.findViewById<TextView>(R.id.txt_pokemon_order)
            val newTxtOrder = when (item.id) {
                in 0..9 -> "00${item.id}"
                in 10..99 -> "0${item.id}"
                else -> "${item.id}"
            }

            txtOrder.text = itemView.context.getString(R.string.pokemon_order, newTxtOrder)

            //Get bg color
            val typeBgColor = getColor(item.types[0].type.name.uppercase())
            val imageResult = item.sprites?.other?.home?.frontDefault ?: ""

            loadingCardWithImage(imageResult, itemView, typeBgColor)

            rvPokemonType = itemView.findViewById(R.id.rv_pokemon_type)
            loadingRecyclerViewPokemonType(item, rvPokemonType, itemView)

            navigateToPokemonDetailsFragment(
                navController,
                R.id.action_nav_home_to_nav_pokemon_details,
                item,
                itemView
            )
        }
    }
}