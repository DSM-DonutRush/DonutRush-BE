package donut.rush.domain.badge.domain

import donut.rush.domain.badge.enums.BadgeType
import java.time.LocalDateTime
import java.util.UUID

data class Badge(
    val id: UUID,
    val userId: UUID,
    val badgeType: BadgeType,
    val earnedAt: LocalDateTime,
)
