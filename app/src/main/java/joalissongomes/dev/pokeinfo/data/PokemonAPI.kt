package joalissongomes.dev.pokeinfo.data

import joalissongomes.dev.pokeinfo.model.Pokemon
import joalissongomes.dev.pokeinfo.model.PokemonDetail
import joalissongomes.dev.pokeinfo.model.PokemonSpecie
import joalissongomes.dev.pokeinfo.model.PokemonType
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonAPI {

    @GET("pokemon")
    fun findAllPokemon(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): Call<Pokemon>

    @GET("pokemon/{name}")
    fun findPokemonDetail(@Path("name") name: String = ""): Call<PokemonDetail>

    @GET("pokemon-species/{name}")
    fun findPokemonSpecieByName(@Path("name") name: String = ""): Call<PokemonSpecie>

    @GET("type/{type}")
    fun findPokemonTypeByType(@Path("type") name: String = ""): Call<PokemonType>
}