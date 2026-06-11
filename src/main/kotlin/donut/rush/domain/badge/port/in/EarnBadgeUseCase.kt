package donut.rush.domain.badge.port.`in`

import donut.rush.domain.badge.enums.BadgeType
import donut.rush.domain.game.domain.GameResult
import java.util.UUID

interface EarnBadgeUseCase {
    fun earnBadges(
        userId: UUID,
        gameResult: GameResult,
    ): List<BadgeType>
}
