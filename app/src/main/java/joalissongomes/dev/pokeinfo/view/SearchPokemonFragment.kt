package joalissongomes.dev.pokeinfo.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import joalissongomes.dev.pokeinfo.R
import joalissongomes.dev.pokeinfo.model.PokemonDetail
import joalissongomes.dev.pokeinfo.presentation.SearchPresenter
import joalissongomes.dev.pokeinfo.utils.getColor
import joalissongomes.dev.pokeinfo.utils.loadingCardWithImage
import joalissongomes.dev.pokeinfo.utils.loadingRecyclerViewPokemonType
import joalissongomes.dev.pokeinfo.utils.navigateToPokemonDetailsFragment

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
                    query?.let {
                        val services =
                            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        services.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)

                        presenter.searchPokemonByName(it)
                    }
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

        //Get bg color
        val typeBgColor = getColor(pokemonDetail.types[0].type.name.uppercase())
        val imageResult = pokemonDetail.sprites?.other?.home?.frontDefault ?: ""

        loadingCardWithImage(imageResult, searchCard, typeBgColor)

        val rvPokemonType = view?.findViewById<RecyclerView>(R.id.rv_pokemon_type)
        if (rvPokemonType != null)
            loadingRecyclerViewPokemonType(pokemonDetail, rvPokemonType, searchCard)

        navigateToPokemonDetailsFragment(
            findNavController(),
            R.id.action_nav_search_to_nav_pokemon_details,
            pokemonDetail,
            searchCard
        )

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
}