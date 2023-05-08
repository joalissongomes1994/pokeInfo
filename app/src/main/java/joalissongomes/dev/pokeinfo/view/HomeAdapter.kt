package joalissongomes.dev.pokeinfo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import joalissongomes.dev.pokeinfo.R
import joalissongomes.dev.pokeinfo.model.PokemonDetail
import joalissongomes.dev.pokeinfo.model.Types
import joalissongomes.dev.pokeinfo.utils.getColor

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

            loadingImageIntoCard(item, typeBgColor)
            loadingRecyclerViewPokemonType(item)
            navigateToPokemonDetailsFragment(item)
        }

        private fun navigateToPokemonDetailsFragment(item: PokemonDetail) {
            itemView.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("pokemonDetails", item)

                navController.navigate(R.id.action_nav_home_to_nav_pokemon_details, bundle)
            }
        }

        // Treating image and card drawable background color
        private fun loadingImageIntoCard(item: PokemonDetail, typeBgColor: Int) {
            val imgPokemonResult = item.sprites?.other?.home?.frontDefault
            val imageView = itemView.findViewById<ImageView>(R.id.img_pokemon)
            val colorResult = ContextCompat.getColor(itemView.context, typeBgColor)

            // bg color in Card
            val colorWithAlpha = ColorUtils.setAlphaComponent(colorResult, 25)
            val drawableCard = ContextCompat.getDrawable(itemView.context, R.drawable.rounded)
            val wrapperDrawableCard = drawableCard?.let { DrawableCompat.wrap(it) }
            wrapperDrawableCard?.let { DrawableCompat.setTint(it, colorWithAlpha) }

            itemView.background = drawableCard

            // bg color and image
            if (imgPokemonResult != null) {
                Picasso.get().load(imgPokemonResult).into(imageView, object : Callback {
                    override fun onSuccess() {
                        val colorWithAlphaToCard = ColorUtils.setAlphaComponent(colorResult, 255)
                        val drawableImg =
                            ContextCompat.getDrawable(itemView.context, R.drawable.rounded_image)
                        val wrappedDrawableImg = drawableImg?.let { DrawableCompat.wrap(it) }
                        wrappedDrawableImg?.let { DrawableCompat.setTint(it, colorWithAlphaToCard) }

                        imageView.background = drawableImg
                    }

                    override fun onError(e: Exception?) {
                        TODO("Not yet implemented")
                    }
                })
            }
        }

        // Loading RV of the Pokemon type
        private fun loadingRecyclerViewPokemonType(item: PokemonDetail) {
            var pokemonTypes = mutableListOf<Types>()
            if (item.types.isNotEmpty()) {
                item.types.map {
                    it.let { type -> pokemonTypes.add(type) }
                }
            }

            rvPokemonType = itemView.findViewById(R.id.rv_pokemon_type)
            rvPokemonType.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = PokemonTypeAdapter(R.layout.item_pokemon_type_card, "LINEAR", pokemonTypes)
            rvPokemonType.adapter = adapter
        }
    }
}