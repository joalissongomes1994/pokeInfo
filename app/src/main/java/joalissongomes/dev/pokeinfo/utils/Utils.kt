package joalissongomes.dev.pokeinfo.utils

import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import joalissongomes.dev.pokeinfo.R

@IntegerRes
fun getColor(type: String): Int {
    return when (type) {
        "NORMAL" -> R.color.gray_200
        "FIGHTING" -> R.color.red_400
        "FLYING" -> R.color.blue_200
        "POISON" -> R.color.lilac_200
        "GROUND" -> R.color.orange_700
        "ROCK" -> R.color.yellow_100
        "BUG" -> R.color.green_500
        "GHOST" -> R.color.purple_400
        "STEEL" -> R.color.teal_200
        "FIRE" -> R.color.orange_500
        "WATER" -> R.color.blue_300
        "GRASS" -> R.color.green_700
        "ELECTRIC" -> R.color.yellow_500
        "PSYCHIC" -> R.color.red_300
        "ICE" -> R.color.teal_200
        "DRAGON" -> R.color.blue_500
        "DARK" -> R.color.gray_500
        "FAIRY" -> R.color.pink_500
        "SHADOW" -> R.color.gray_400
        else -> R.color.gray_400 //UNKNOWN
    }
}

@IntegerRes
fun getTextColor(type: String): Int {
    return when (type) {
        "ELECTRIC" -> R.color.gray_500
        else -> R.color.white
    }
}

@DrawableRes
fun getDrawable(type: String): Int {
    return when (type) {
        "NORMAL" -> R.drawable.normal
        "FIGHTING" -> R.drawable.fighting
        "FLYING" -> R.drawable.flying
        "POISON" -> R.drawable.poison
        "GROUND" -> R.drawable.ground
        "ROCK" -> R.drawable.rock
        "BUG" -> R.drawable.bug
        "GHOST" -> R.drawable.ghost
        "STEEL" -> R.drawable.steel
        "FIRE" -> R.drawable.fire
        "WATER" -> R.drawable.water
        "GRASS" -> R.drawable.grass
        "ELECTRIC" -> R.drawable.electric
        "PSYCHIC" -> R.drawable.psychic
        "ICE" -> R.drawable.ice
        "DRAGON" -> R.drawable.dragon
        "DARK" -> R.drawable.dark
        "FAIRY" -> R.drawable.fairy
        "SHADOW" -> R.drawable.dark
        else -> R.drawable.dark //UNKNOWN
    }
}