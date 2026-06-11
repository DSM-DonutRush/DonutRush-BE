package donut.rush.domain.ranking.enums

enum class RankingType {
    OVERALL,
    WEEKLY,
    ;

    companion object {
        fun from(value: String): RankingType =
            entries.firstOrNull { it.name.equals(value, ignoreCase = true) }
                ?: throw IllegalArgumentException("Unsupported ranking type: $value")
    }
}
