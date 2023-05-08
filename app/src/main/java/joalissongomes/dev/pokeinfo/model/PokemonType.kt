package joalissongomes.dev.pokeinfo.model

import com.google.gson.annotations.SerializedName

data class PokemonType(
    val id: Int,
    val name: String,
    @SerializedName("damage_relations") val damageRelations: DamageRelations
)

data class DamageRelations(
    @SerializedName("double_damage_from") val doubleDamageFrom: List<DoubleDamageFrom>
)

data class DoubleDamageFrom(
    val name: String,
    val url: String
)