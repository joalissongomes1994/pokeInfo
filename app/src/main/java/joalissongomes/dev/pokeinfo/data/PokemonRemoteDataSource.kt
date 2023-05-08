package joalissongomes.dev.pokeinfo.data

import joalissongomes.dev.pokeinfo.model.PokemonDetail
import joalissongomes.dev.pokeinfo.model.PokemonSpecie
import joalissongomes.dev.pokeinfo.model.PokemonType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
                        service.findPokemonDetail(name).await()
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

    fun findPokemonDetails(callback: PokemonDetailsCallback, name: String, type: String) {

        service.findPokemonSpecieByName(name.lowercase()).enqueue(object : Callback<PokemonSpecie> {
            override fun onResponse(call: Call<PokemonSpecie>, response: Response<PokemonSpecie>) {
                if(response.isSuccessful) {
                    val result = response.body()
                    result?.let { callback.onSuccessPokemonSpecie(it) }
                } else {
                    val error = JSONObject(response.errorBody()?.string() ?: "")
                    val message = error.getString("message")
                    callback.onErrorPokemonSpecie(message ?: "Unknown error")
                }

                callback.onCompletePokemonSpecie()
            }

            override fun onFailure(call: Call<PokemonSpecie>, t: Throwable) {
                callback.onErrorPokemonSpecie(t.message ?: "Internal error")
                callback.onCompletePokemonSpecie()
            }
        })

        service.findPokemonTypeByType(type.lowercase()).enqueue(object : Callback<PokemonType> {
            override fun onResponse(call: Call<PokemonType>, response: Response<PokemonType>) {
                if(response.isSuccessful) {
                    val result = response.body()
                    result?.let { callback.onSuccessPokemonType(it) }
                }else {
                    response.errorBody()?.let {
                        callback.onErrorPokemonType(it.string())
                    }
                }
                callback.onCompletePokemonType()
            }

            override fun onFailure(call: Call<PokemonType>, t: Throwable) {
                callback.onErrorPokemonType(t.message ?: "Internal error.")
                callback.onCompletePokemonType()
            }
        })
    }
}