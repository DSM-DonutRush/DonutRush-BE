package donut.rush.domain.badge.service

import donut.rush.domain.badge.domain.Badge
import donut.rush.domain.badge.enums.BadgeType
import donut.rush.domain.badge.port.`in`.EarnBadgeUseCase
import donut.rush.domain.badge.port.`in`.GetBadgeUseCase
import donut.rush.domain.badge.port.out.BadgePort
import donut.rush.domain.badge.presentation.dto.response.BadgeResponse
import donut.rush.domain.game.domain.GameResult
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class BadgeService(
    private val badgePort: BadgePort,
) : EarnBadgeUseCase,
    GetBadgeUseCase {
    override fun earnBadges(
        userId: UUID,
        gameResult: GameResult,
    ): List<BadgeType> =
        BadgeType.entries
            .filter { it.isSatisfied(gameResult) }
            .filterNot { badgePort.existsByUserIdAndBadgeType(userId, it) }
            .onEach { badgeType ->
                badgePort.save(
                    Badge(
                        id = UUID.randomUUID(),
                        userId = userId,
                        badgeType = badgeType,
                        earnedAt = LocalDateTime.now(),
                    ),
                )
            }

    override fun getBadges(userId: UUID): List<BadgeResponse> {
        val earned = badgePort.findByUserId(userId).associateBy { it.badgeType }
        return BadgeType.entries.map { badgeType ->
            val badge = earned[badgeType]
            if (badge != null) {
                BadgeResponse.earned(badgeType, badge.earnedAt)
            } else {
                BadgeResponse.notEarned(badgeType)
            }
        }
    }
}
