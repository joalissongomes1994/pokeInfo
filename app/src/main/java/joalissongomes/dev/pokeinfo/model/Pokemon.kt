package joalissongomes.dev.pokeinfo.model

data class ResultPokemon(
    val name: String?,
    val url: String?
)

data class Pokemon(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<ResultPokemon>
)