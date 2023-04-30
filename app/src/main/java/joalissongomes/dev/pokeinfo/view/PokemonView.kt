package joalissongomes.dev.pokeinfo.view

import joalissongomes.dev.pokeinfo.model.PokemonDetail

interface PokemonView {
    fun showPokemonDetail(pokemonListResult: List<PokemonDetail>, count: Int)
    fun showFailurePokemonDetail(message: String)
    fun showProgressBar()
    fun hideProgressBar()
}
