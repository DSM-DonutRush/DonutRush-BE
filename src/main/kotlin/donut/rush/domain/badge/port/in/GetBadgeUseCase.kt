package donut.rush.domain.badge.port.`in`

import donut.rush.domain.badge.presentation.dto.response.BadgeResponse
import java.util.UUID

interface GetBadgeUseCase {
    fun getBadges(userId: UUID): List<BadgeResponse>
}
