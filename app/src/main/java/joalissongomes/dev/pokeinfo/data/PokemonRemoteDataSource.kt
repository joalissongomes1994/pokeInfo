package joalissongomes.dev.pokeinfo.data

import joalissongomes.dev.pokeinfo.model.PokemonDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.await

class PokemonRemoteDataSource {

    private val service: PokemonAPI = HTTPClient.retrofit().create(PokemonAPI::class.java)
    private val scope = CoroutineScope(Dispatchers.Main)


    suspend fun findPokemonList(callback: PokemonCallback, offset: Int = 0, limit: Int = 20) {
        try {
            val response = service.findAllPokemon(offset, limit).await()

            val jobs = response.results.map { item ->
                item.name?.let { name ->
                    scope.async {
                        findPokemonDetailByName(name)
                    }
                }
            }

            val result = jobs.map { it?.await() }
            if (result.isNotEmpty()) {
                callback.onSuccess(result as List<PokemonDetail>, response.count)
            }

            callback.onComplete()

        } catch (e: Exception) {
            callback.onError(e.message ?: "Internal error")
            callback.onComplete()
        }
    }

    private suspend fun findPokemonDetailByName(name: String = ""): PokemonDetail {
        return service.findPokemonDetail(name).await()
    }
}

