package donut.rush.domain.badge.port.out

import donut.rush.domain.badge.domain.Badge
import donut.rush.domain.badge.enums.BadgeType
import java.util.UUID

interface BadgePort {
    fun findByUserId(userId: UUID): List<Badge>

    fun findByUserIdIn(userIds: List<UUID>): List<Badge>

    fun existsByUserIdAndBadgeType(
        userId: UUID,
        badgeType: BadgeType,
    ): Boolean

    fun save(badge: Badge): Badge
}
