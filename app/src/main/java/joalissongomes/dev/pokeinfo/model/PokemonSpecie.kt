package joalissongomes.dev.pokeinfo.model

data class PokemonSpecie(
    val base_happiness: Int,
    val capture_rate: Int,
    val color: PokemonColor,
    val egg_groups: List<PokemonEggGroup>,
    val evolution_chain: PokemonEvolutionChain,
    val flavor_text_entries: List<PokemonFlavorTextEntry>,
    val forms_switchable: Boolean,
    val gender_rate: Int,
    val genera: List<PokemonGenus>,
    val generation: PokemonGeneration,
    val growth_rate: PokemonGrowthRate,
    val habitat: PokemonHabitat,
    val has_gender_differences: Boolean,
    val hatch_counter: Int,
    val id: Int,
    val is_baby: Boolean,
    val is_legendary: Boolean,
    val is_mythical: Boolean,
    val name: String,
    val shape: PokemonShape
)

data class PokemonColor(
    val name: String,
    val url: String
)

data class PokemonEggGroup(
    val name: String,
    val url: String
)

data class PokemonEvolutionChain(
    val url: String
)

data class PokemonFlavorTextEntry(
    val flavor_text: String,
    val language: PokemonLanguage,
    val version: PokemonVersion
)

data class PokemonLanguage(
    val name: String,
    val url: String
)

data class PokemonVersion(
    val name: String,
    val url: String
)

data class PokemonGenus(
    val genus: String,
    val language: PokemonLanguage
)

data class PokemonGeneration(
    val name: String,
    val url: String
)

data class PokemonGrowthRate(
    val name: String,
    val url: String
)

data class PokemonHabitat(
    val name: String,
    val url: String
)

data class PokemonShape(
    val name: String,
    val url: String
)