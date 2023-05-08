package joalissongomes.dev.pokeinfo.view

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import joalissongomes.dev.pokeinfo.R
import joalissongomes.dev.pokeinfo.model.*
import joalissongomes.dev.pokeinfo.presentation.PokemonDetailsPresenter
import joalissongomes.dev.pokeinfo.utils.GridSpacingItemDecoration
import joalissongomes.dev.pokeinfo.utils.getBgIconFromDrawable
import joalissongomes.dev.pokeinfo.utils.getColor
import joalissongomes.dev.pokeinfo.utils.replaceFirstCharToUppercase

class PokemonDetailsFragment : Fragment(), PokemonDetailsView {

    private lateinit var rvPokemonType: RecyclerView
    private lateinit var adapterType: PokemonTypeAdapter

    private lateinit var rvPokemonDamageType: RecyclerView
    private lateinit var adapterDamageType: PokemonTypeAdapter

    private lateinit var presenter: PokemonDetailsPresenter
    private lateinit var pokemonDetails: PokemonDetail
    private lateinit var typeResult: String

    private val pokemonDamageTypes = mutableListOf<Types>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = PokemonDetailsPresenter(this)
        pokemonDetails = arguments?.getSerializable("pokemonDetails") as PokemonDetail
        typeResult = pokemonDetails.types[0].type.name.uppercase()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pokemon_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.findPokemonSpecieByName(pokemonDetails.name, typeResult)

        val txtTitle = view.findViewById<TextView>(R.id.txt_pokemon_title)
        val txtOrder = view.findViewById<TextView>(R.id.txt_pokemon_order)

        val newTxtOrder = when (pokemonDetails.id) {
            in 0..9 -> "00${pokemonDetails.id}"
            in 10..99 -> "0${pokemonDetails.id}"
            else -> pokemonDetails.id.toString()
        }

        txtTitle.text = replaceFirstCharToUppercase(pokemonDetails.name)
        txtOrder.text = context?.getString(R.string.pokemon_order, newTxtOrder)

        showImageCover()
        showCharacteristicData(view)
        showRecyclerViewPokemonType(pokemonDetails, view)
        showRecyclerViewPokemonDamageType(view)
    }

    // modify drawable wrap
    private fun changeDrawableWrap(drawable: Drawable, @ColorInt color: Int) {
        val drawableWrapped = drawable.let { DrawableCompat.wrap(it) }
        drawableWrapped?.let { DrawableCompat.setTint(it, color) }
    }

    private fun showCharacteristicData(view: View) {
        val editWeight = view.findViewById<EditText>(R.id.edit_weight)
        val editHeight = view.findViewById<EditText>(R.id.edit_height)

        val drawableEditText = view.context?.let {
            ContextCompat.getDrawable(it, R.drawable.rounded_edit_text)
        }

        val colorResult = ContextCompat.getColor(requireContext(), getColor(typeResult))
        drawableEditText?.let { changeDrawableWrap(it, colorResult) }

        editWeight.setText(getString(R.string.edit_weight, pokemonDetails.weight.toString()))
        editHeight.setText(getString(R.string.edit_height, pokemonDetails.height.toString()))

        editHeight.background = drawableEditText
        editWeight.background = drawableEditText
    }

    private fun showImageCover() {
        val imgCoverDetail = view?.findViewById<ImageView>(R.id.img_cover_details)
        val typeResult = pokemonDetails.types[0].type.name.uppercase()
        val urlResult = pokemonDetails.sprites?.other?.home?.frontDefault

        val drawableBgIcon = getBgIconFromDrawable(typeResult)
        val bgColorResult = getColor(typeResult)

        if (urlResult != null) {
            Picasso.get().load(urlResult).into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    bitmap?.let {
                        val layerDrawable = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.drawable_pokemon_details
                        ) as LayerDrawable
                        val bgColor = ContextCompat.getColor(requireContext(), bgColorResult)
                        val middleLayer =
                            ContextCompat.getDrawable(requireContext(), drawableBgIcon)

                        val gradientDrawable = GradientDrawable()
                        gradientDrawable.setColor(bgColor)

                        val bitmapDrawable = BitmapDrawable(view?.resources, it)

                        layerDrawable.setDrawableByLayerId(R.id.lower_layer, gradientDrawable)
                        layerDrawable.setDrawableByLayerId(R.id.middle_layer, middleLayer)
                        layerDrawable.setDrawableByLayerId(R.id.upper_layer, bitmapDrawable)

                        imgCoverDetail?.setImageDrawable(layerDrawable)
                        imgCoverDetail?.setBackgroundColor(bgColor)
                    }
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            })
        }
    }

    // Loading RV of the Pokemon type
    private fun showRecyclerViewPokemonType(item: PokemonDetail, view: View) {
        val pokemonTypes = mutableListOf<Types>()
        if (item.types.isNotEmpty()) {
            item.types.map {
                it.let { type -> pokemonTypes.add(type) }
            }
        }

        rvPokemonType = view.findViewById(R.id.rv_pokemon_type)
        rvPokemonType.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        adapterType = PokemonTypeAdapter(R.layout.item_pokemon_type_card, "LINEAR", pokemonTypes)
        rvPokemonType.adapter = adapterType
    }

    // Loading RV of the Pokemon type
    private fun showRecyclerViewPokemonDamageType(view: View) {
        rvPokemonDamageType = view.findViewById(R.id.rv_pokemon_damage_type)
        val layoutManager = GridLayoutManager(view.context, 2, GridLayoutManager.VERTICAL, false)
        rvPokemonDamageType.layoutManager = layoutManager

        adapterDamageType =
            PokemonTypeAdapter(R.layout.item_pokemon_type_card, "GRID", pokemonDamageTypes)
        rvPokemonDamageType.adapter = adapterDamageType

        val spanCount = 2 // number of columns
        val spacing = 8 // spacing in pixels
        val includeEdge = true // include spacing to border of RecyclerView

        rvPokemonDamageType.addItemDecoration(
            GridSpacingItemDecoration(
                spanCount,
                spacing,
                includeEdge
            )
        )
    }

    override fun showSpecieInfo(specie: PokemonSpecie) {
        val txtDescription = view?.findViewById<TextView>(R.id.txt_pokemon_description)
        val textDescriptionResult = specie.flavor_text_entries.findLast { it.language.name == "en" }
        txtDescription?.text = textDescriptionResult?.flavor_text?.replace("\n", " ")

        val editCategory = view?.findViewById<EditText>(R.id.edit_category)
        val editHabitat = view?.findViewById<EditText>(R.id.edit_habitat)

        val drawableEditText = view?.context?.let {
            ContextCompat.getDrawable(it, R.drawable.rounded_edit_text)
        }

        val colorResult = ContextCompat.getColor(requireContext(), getColor(typeResult))
        drawableEditText?.let { changeDrawableWrap(it, colorResult) }

        editCategory?.setText(replaceFirstCharToUppercase(specie.shape.name))
        editHabitat?.setText(replaceFirstCharToUppercase(specie.habitat.name))

        editCategory?.background = drawableEditText
        editHabitat?.background = drawableEditText
    }

    override fun showTypeInfo(pokemonType: PokemonType) {
        pokemonType.damageRelations.doubleDamageFrom.let { types ->
            types.map { item ->
                val type = Type(name = item.name, url = item.url)
                val types = Types(slot = null, type = type)
                pokemonDamageTypes.add(types)
            }
        }

        adapterDamageType.notifyDataSetChanged()
    }

    override fun showFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showProgress() {}

    override fun hideProgress() {}
}

