package donut.rush.domain.badge.presentation.dto.response

import donut.rush.domain.badge.enums.BadgeType
import java.time.LocalDateTime

data class BadgeResponse(
    val badgeType: BadgeType,
    val displayName: String,
    val earnedAt: LocalDateTime?,
    val earned: Boolean,
) {
    companion object {
        fun earned(
            badgeType: BadgeType,
            earnedAt: LocalDateTime,
        ) = BadgeResponse(badgeType = badgeType, displayName = badgeType.displayName, earnedAt = earnedAt, earned = true)

        fun notEarned(badgeType: BadgeType) =
            BadgeResponse(badgeType = badgeType, displayName = badgeType.displayName, earnedAt = null, earned = false)
    }
}
