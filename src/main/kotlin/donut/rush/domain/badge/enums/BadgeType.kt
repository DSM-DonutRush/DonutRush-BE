package donut.rush.domain.badge.enums

import donut.rush.domain.game.domain.GameResult

enum class BadgeType(
    val displayName: String,
) {
    BASIC_CLEAR("기본 클리어") {
        override fun isSatisfied(result: GameResult): Boolean = result.success
    },
    NO_PENALTY("무실수") {
        override fun isSatisfied(result: GameResult): Boolean = result.success && result.penaltyCount == 0
    },
    SPEED("스피드") {
        override fun isSatisfied(result: GameResult): Boolean = result.success && result.clearTime < SPEED_CLEAR_THRESHOLD
    },
    ;

    abstract fun isSatisfied(result: GameResult): Boolean

    companion object {
        const val SPEED_CLEAR_THRESHOLD = 60_000L
    }
}
