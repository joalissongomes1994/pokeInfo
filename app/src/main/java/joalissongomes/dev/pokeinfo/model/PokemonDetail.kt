package joalissongomes.dev.pokeinfo.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Ability(
    val name: String,
    val url: String
)

data class Abilities(
    val ability: Ability,
    @SerializedName("is_hidden") val isHidden: Boolean,
    val slot: Int
)

data class Species(
    val name: String,
    val url: String
)

data class Home(@SerializedName("front_default") val frontDefault: String?)
data class Other(val home: Home?)
data class Sprite(val other: Other?)

data class Stat(
    val name: String,
    val url: String
)

data class Stats(
    @SerializedName("base_stat") val baseStat: Int,
    val effort: Int,
    val stat: Stat
)

data class Type(
    val name: String,
    val url: String
)

data class Types(val slot: Int?, val type: Type)

data class PokemonDetail(
    val id: Int,
    val abilities: List<Abilities>,
    @SerializedName("base_experience") val baseExperience: Int,
    val height: Int,
    val weight: Int,
    val name: String,
    val order: Int,
    val species: Species,
    val sprites: Sprite?,
    val stats: List<Stats>,
    val types: List<Types>
) : Serializable
