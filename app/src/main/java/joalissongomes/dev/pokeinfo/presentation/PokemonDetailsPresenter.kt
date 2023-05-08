package joalissongomes.dev.pokeinfo.presentation

import joalissongomes.dev.pokeinfo.data.PokemonDetailsCallback
import joalissongomes.dev.pokeinfo.data.PokemonRemoteDataSource
import joalissongomes.dev.pokeinfo.model.PokemonSpecie
import joalissongomes.dev.pokeinfo.model.PokemonType
import joalissongomes.dev.pokeinfo.view.PokemonDetailsView

class PokemonDetailsPresenter(
    private val pokemonDetailsView: PokemonDetailsView,
    private val dataSource: PokemonRemoteDataSource = PokemonRemoteDataSource()
) : PokemonDetailsCallback {

    fun findPokemonSpecieByName(name: String, type: String) {
        pokemonDetailsView.showProgress()

        dataSource.findPokemonDetails(this, name, type)
    }


    override fun onSuccessPokemonSpecie(pokemonSpecie: PokemonSpecie) {
        pokemonDetailsView.showSpecieInfo(pokemonSpecie)
    }

    override fun onSuccessPokemonType(pokemonType: PokemonType) {
        pokemonDetailsView.showTypeInfo(pokemonType)
    }

    override fun onErrorPokemonSpecie(message: String) {
        pokemonDetailsView.showFailure(message)
    }

    override fun onErrorPokemonType(message: String) {
        pokemonDetailsView.showFailure(message)
    }

    override fun onCompletePokemonSpecie() {
        pokemonDetailsView.hideProgress()
    }

    override fun onCompletePokemonType() {
        pokemonDetailsView.hideProgress()
    }
}