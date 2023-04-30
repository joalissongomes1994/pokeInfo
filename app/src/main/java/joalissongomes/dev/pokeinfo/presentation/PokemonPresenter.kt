package joalissongomes.dev.pokeinfo.presentation

import joalissongomes.dev.pokeinfo.data.PokemonCallback
import joalissongomes.dev.pokeinfo.data.PokemonRemoteDataSource
import joalissongomes.dev.pokeinfo.model.PokemonDetail
import joalissongomes.dev.pokeinfo.view.PokemonView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonPresenter(
    private val pokemonView: PokemonView,
    private val dataSource: PokemonRemoteDataSource = PokemonRemoteDataSource()
) : PokemonCallback {

    suspend fun findPokemonDetail(offset: Int) {
        val scope = CoroutineScope(Dispatchers.Main)

        pokemonView.showProgressBar()
        scope.launch {
            dataSource.findPokemonList(this@PokemonPresenter, offset)
        }
    }

    override fun onSuccess(response: List<PokemonDetail>, count: Int) {
        response.let { pokemonView.showPokemonDetail(it, count) }
    }

    override fun onError(message: String) {
        pokemonView.showFailurePokemonDetail(message)
    }

    override fun onComplete() {
        pokemonView.hideProgressBar()
    }
}