package joalissongomes.dev.pokeinfo.view

import joalissongomes.dev.pokeinfo.model.PokemonSpecie
import joalissongomes.dev.pokeinfo.model.PokemonType

interface PokemonDetailsView {
    fun showSpecieInfo(pokemonSpecie: PokemonSpecie)
    fun showTypeInfo(pokemonType: PokemonType)

    fun showFailure(message: String)

    fun showProgress()
    fun hideProgress()
}