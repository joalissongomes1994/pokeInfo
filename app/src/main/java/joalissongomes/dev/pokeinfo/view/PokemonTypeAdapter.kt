package joalissongomes.dev.pokeinfo.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import joalissongomes.dev.pokeinfo.R
import joalissongomes.dev.pokeinfo.model.Types
import joalissongomes.dev.pokeinfo.utils.getColor
import joalissongomes.dev.pokeinfo.utils.getDrawable
import joalissongomes.dev.pokeinfo.utils.getTextColor

class PokemonTypeAdapter(
    @LayoutRes private val layoutId: Int,
    private val typeLayoutManager: String,
    private val pokemonTypeList: List<Types>
) :
    RecyclerView.Adapter<PokemonTypeAdapter.PokemonTypeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonTypeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)

        if (typeLayoutManager == "GRID") {
            val layoutParams = view.layoutParams as GridLayoutManager.LayoutParams
            layoutParams.width = parent.width / 2 - 24
            layoutParams.height = 80
            view.layoutParams = layoutParams
        }

        return PokemonTypeViewHolder(view)

    }

    override fun onBindViewHolder(holder: PokemonTypeViewHolder, position: Int) {
        val itemCurrent = pokemonTypeList[position]
        holder.bind(itemCurrent)

        if (typeLayoutManager == "LINEAR") {
            val layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
            )

            if (position < pokemonTypeList.size - 1) {
                layoutParams.marginEnd = 24
            }

            layoutParams.height = 80
            holder.itemView.layoutParams = layoutParams
        }
    }

    override fun getItemCount(): Int {
        return pokemonTypeList.size
    }

    inner class PokemonTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Types) {
            val txtPokemonType = itemView.findViewById<TextView>(R.id.txt_pokemon_type)

            val textColorItem = getTextColor(item.type.name.uppercase())
            val txtColor = ContextCompat.getColor(itemView.context, textColorItem as Int)
            txtPokemonType.setTextColor(txtColor)
            txtPokemonType.text = item.type.name.lowercase().replaceFirstChar { it.uppercase() }

            val drawableType = getDrawable(item.type.name.uppercase())
            val bgColorItem = getColor(item.type.name.uppercase())

            val colorResult = ContextCompat.getColor(itemView.context, bgColorItem as Int)

            // bg color in drawable of the imageView
            val colorWithAlphaToCard = ColorUtils.setAlphaComponent(colorResult, 255)
            val drawableImg = ContextCompat.getDrawable(itemView.context, R.drawable.rounded_image)
            val wrappedDrawableImg = drawableImg?.let { DrawableCompat.wrap(it) }
            wrappedDrawableImg?.let { DrawableCompat.setTint(it, colorWithAlphaToCard) }
            itemView.background = drawableImg

            // Swapping image from within the drawable of the imageView tag directly
            val imgPokemonType = itemView.findViewById<ImageView>(R.id.img_pokemon_type)
            val drawable = ContextCompat.getDrawable(itemView.context, drawableType)
            imgPokemonType.setImageDrawable(drawable)
        }


    }
}