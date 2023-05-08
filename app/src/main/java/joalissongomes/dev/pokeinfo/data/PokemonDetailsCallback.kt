package joalissongomes.dev.pokeinfo.data

import joalissongomes.dev.pokeinfo.model.PokemonSpecie
import joalissongomes.dev.pokeinfo.model.PokemonType

interface PokemonDetailsCallback {
    fun onSuccessPokemonSpecie(pokemonSpecie: PokemonSpecie)
    fun onSuccessPokemonType(pokemonType: PokemonType)

    fun onErrorPokemonSpecie(message: String)
    fun onErrorPokemonType(message: String)

    fun onCompletePokemonSpecie()
    fun onCompletePokemonType()
}