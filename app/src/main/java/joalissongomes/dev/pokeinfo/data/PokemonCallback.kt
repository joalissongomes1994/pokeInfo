package joalissongomes.dev.pokeinfo.data

import joalissongomes.dev.pokeinfo.model.PokemonDetail

interface PokemonCallback {
    fun onSuccess(response: List<PokemonDetail>, count: Int)
    fun onError(message: String)
    fun onComplete()
}