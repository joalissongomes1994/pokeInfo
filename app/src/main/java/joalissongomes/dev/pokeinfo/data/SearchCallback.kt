package joalissongomes.dev.pokeinfo.data

import joalissongomes.dev.pokeinfo.model.PokemonDetail

interface SearchCallback {
    fun onSuccess(pokemonSpecie: PokemonDetail)
    fun onError(message: String)
    fun onComplete()
}