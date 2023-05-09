package joalissongomes.dev.pokeinfo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import joalissongomes.dev.pokeinfo.R
import joalissongomes.dev.pokeinfo.model.PokemonDetail
import joalissongomes.dev.pokeinfo.model.Types
import joalissongomes.dev.pokeinfo.presentation.SearchPresenter
import joalissongomes.dev.pokeinfo.utils.getColor

class SearchPokemonFragment : Fragment(), SearchView {
    private lateinit var presenter: SearchPresenter
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var searchCard: View
    private lateinit var progressBar: ProgressBar

    private lateinit var txtOrder: TextView
    private lateinit var txtName: TextView
    private lateinit var img: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_pokemon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = SearchPresenter(this)

        progressBar = view.findViewById(R.id.progress_bar_search)

        txtOrder = view.findViewById(R.id.txt_pokemon_order)
        txtName = view.findViewById(R.id.txt_pokemon_name)
        img = view.findViewById(R.id.img_pokemon)

        searchView = view.findViewById(R.id.search_id)

        searchCard = view.findViewById(R.id.search_card)

        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                val validate = query?.trim()?.length ?: 0
                if (validate > 0) {
                    query?.let { presenter.searchPokemonByName(it) }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val validate = newText?.trim()?.length ?: 0
                if (validate == 0) {
                    searchCard.visibility = View.GONE
                }
                return true
            }
        })
    }

    override fun showSuccess(pokemonDetail: PokemonDetail) {
        val newTxtOrder = when (pokemonDetail.id) {
            in 0..9 -> "00${pokemonDetail.id}"
            in 10..99 -> "0${pokemonDetail.id}"
            else -> "${pokemonDetail.id}"
        }

        loadingImage(pokemonDetail)
        loadingRecyclerViewPokemonType(pokemonDetail)
        navigateToPokemonDetailsFragment(pokemonDetail)

        txtOrder.text = getString(R.string.pokemon_order, newTxtOrder)
        txtName.text = pokemonDetail.name.lowercase().replaceFirstChar { it.uppercase() }

        searchCard.visibility = View.VISIBLE
    }

    override fun showFailure(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        searchCard.visibility = View.GONE
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun loadingImage(pokemonDetail: PokemonDetail) {
        val url = pokemonDetail.sprites?.other?.home?.frontDefault
        val typeBgColor = getColor(pokemonDetail.types[0].type.name.uppercase())
        val colorResult = ContextCompat.getColor(requireContext(), typeBgColor)

        val colorWithAlphaCard = ColorUtils.setAlphaComponent(colorResult, 25)
        val drawableCard = ContextCompat.getDrawable(requireContext(), R.drawable.rounded)
        val wrappedDrawableCard = drawableCard?.let { DrawableCompat.wrap(it) }
        wrappedDrawableCard?.let { DrawableCompat.setTint(it, colorWithAlphaCard) }
        val layoutCard = view?.findViewById<View>(R.id.search_card)
        layoutCard?.background = drawableCard

        if (url != null) {
            Picasso.get().load(url).into(img, object : Callback {
                override fun onSuccess() {
                    val drawableImage =
                        ContextCompat.getDrawable(requireContext(), R.drawable.rounded_image)
                    val wrappedDrawableImage = drawableImage?.let { DrawableCompat.wrap(it) }
                    wrappedDrawableImage?.let { DrawableCompat.setTint(it, colorResult) }

                    img.background = drawableImage
                }

                override fun onError(e: Exception?) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun loadingRecyclerViewPokemonType(pokemonDetail: PokemonDetail) {
        val pokemonTypes = mutableListOf<Types>()
        if (pokemonDetail.types.isNotEmpty()) {
            pokemonDetail.types.map {
                it.let { type -> pokemonTypes.add(type) }
            }
        }

        val rvPokemonType = view?.findViewById<RecyclerView>(R.id.rv_pokemon_type)
        rvPokemonType?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val adapter = PokemonTypeAdapter(R.layout.item_pokemon_type_card, "LINEAR", pokemonTypes)
        rvPokemonType?.adapter = adapter
    }

    private fun navigateToPokemonDetailsFragment(pokemonDetail: PokemonDetail) {
        searchCard.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("pokemonDetails", pokemonDetail)

            findNavController().navigate(R.id.action_nav_search_to_nav_pokemon_details, bundle)
        }
    }
}