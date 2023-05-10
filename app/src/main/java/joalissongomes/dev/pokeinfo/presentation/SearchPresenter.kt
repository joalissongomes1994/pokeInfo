package joalissongomes.dev.pokeinfo.presentation

import joalissongomes.dev.pokeinfo.data.PokemonRemoteDataSource
import joalissongomes.dev.pokeinfo.data.SearchCallback
import joalissongomes.dev.pokeinfo.model.PokemonDetail
import joalissongomes.dev.pokeinfo.view.SearchView

class SearchPresenter(
    private val searchView: SearchView,
    private val dataSource: PokemonRemoteDataSource = PokemonRemoteDataSource()
): SearchCallback {

    fun searchPokemonByName(name: String) {
        searchView.showProgress()

        dataSource.searchPokemonByName(this, name)
    }

    override fun onSuccess(pokemonDetail: PokemonDetail) {
        searchView.showSuccess(pokemonDetail)
    }

    override fun onError(message: String) {
        searchView.showFailure(message)
    }

    override fun onComplete() {
        searchView.hideProgress()
    }
}