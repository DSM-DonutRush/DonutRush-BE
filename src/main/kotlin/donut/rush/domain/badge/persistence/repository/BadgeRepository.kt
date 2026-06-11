package donut.rush.domain.badge.persistence.repository

import donut.rush.domain.badge.entity.BadgeJpaEntity
import donut.rush.domain.badge.enums.BadgeType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BadgeRepository : JpaRepository<BadgeJpaEntity, UUID> {
    fun findByUser_Id(userId: UUID): List<BadgeJpaEntity>

    fun findByUser_IdIn(userIds: List<UUID>): List<BadgeJpaEntity>

    fun existsByUser_IdAndBadgeType(
        userId: UUID,
        badgeType: BadgeType,
    ): Boolean
}
