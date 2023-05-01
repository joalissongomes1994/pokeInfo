package joalissongomes.dev.pokeinfo.presentation

import joalissongomes.dev.pokeinfo.data.PokemonCallback
import joalissongomes.dev.pokeinfo.data.PokemonRemoteDataSource
import joalissongomes.dev.pokeinfo.model.PokemonDetail
import joalissongomes.dev.pokeinfo.view.HomeView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomePresenter(
    private val pokemonView: HomeView,
    private val dataSource: PokemonRemoteDataSource = PokemonRemoteDataSource()
) : PokemonCallback {

    suspend fun findPokemonDetail(offset: Int) {
        val scope = CoroutineScope(Dispatchers.Main)

        pokemonView.showProgressBar()
        scope.launch {
            dataSource.findPokemonList(this@HomePresenter, offset)
        }
    }

    override fun onSuccess(response: List<PokemonDetail>, total: Int) {
        response.let { pokemonView.showPokemonDetail(it, total) }
    }

    override fun onError(message: String) {
        pokemonView.showFailurePokemonDetail(message)
    }

    override fun onComplete() {
        pokemonView.hideProgressBar()
    }
}