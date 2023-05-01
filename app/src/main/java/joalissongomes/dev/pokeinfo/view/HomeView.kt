package joalissongomes.dev.pokeinfo.view

import joalissongomes.dev.pokeinfo.model.PokemonDetail

interface HomeView {
    fun showPokemonDetail(pokemonListResult: List<PokemonDetail>, total: Int)
    fun showFailurePokemonDetail(message: String)
    fun showProgressBar()
    fun hideProgressBar()
}