package donut.rush.domain.badge.persistence

import donut.rush.domain.badge.domain.Badge
import donut.rush.domain.badge.entity.BadgeJpaEntity
import donut.rush.domain.badge.enums.BadgeType
import donut.rush.domain.badge.mapper.BadgeMapper
import donut.rush.domain.badge.persistence.repository.BadgeRepository
import donut.rush.domain.badge.port.out.BadgePort
import donut.rush.domain.user.persistence.repository.UserRepository
import donut.rush.global.error.exception.DonutRushException
import donut.rush.global.error.exception.ErrorCode
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class BadgePersistenceAdapter(
    private val badgeRepository: BadgeRepository,
    private val userRepository: UserRepository,
    private val badgeMapper: BadgeMapper,
) : BadgePort {
    override fun findByUserId(userId: UUID): List<Badge> = badgeRepository.findByUser_Id(userId).map { badgeMapper.toDomain(it) }

    override fun findByUserIdIn(userIds: List<UUID>): List<Badge> =
        badgeRepository.findByUser_IdIn(userIds).map { badgeMapper.toDomain(it) }

    override fun existsByUserIdAndBadgeType(
        userId: UUID,
        badgeType: BadgeType,
    ): Boolean = badgeRepository.existsByUser_IdAndBadgeType(userId, badgeType)

    override fun save(badge: Badge): Badge {
        val user =
            userRepository
                .findById(badge.userId)
                .orElseThrow { DonutRushException(ErrorCode.USER_NOT_FOUND) }
        val entity =
            BadgeJpaEntity(
                user = user,
                badgeType = badge.badgeType,
                earnedAt = badge.earnedAt,
            )
        return badgeMapper.toDomain(badgeRepository.save(entity))
    }
}
