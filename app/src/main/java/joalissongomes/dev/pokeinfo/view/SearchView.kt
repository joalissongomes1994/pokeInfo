package joalissongomes.dev.pokeinfo.view

import joalissongomes.dev.pokeinfo.model.PokemonDetail

interface SearchView {
    fun showSuccess(pokemonDetail: PokemonDetail)
    fun showFailure(message: String)
    fun showProgress()
    fun hideProgress()
}