package donut.rush.domain.badge.mapper

import donut.rush.domain.badge.domain.Badge
import donut.rush.domain.badge.entity.BadgeJpaEntity
import donut.rush.global.base.GenericMapper
import org.springframework.stereotype.Component

@Component
class BadgeMapper : GenericMapper<Badge, BadgeJpaEntity> {
    override fun toDomain(entity: BadgeJpaEntity): Badge =
        Badge(
            id = entity.id,
            userId = entity.user.id,
            badgeType = entity.badgeType,
            earnedAt = entity.earnedAt,
        )

    override fun toEntity(domain: Badge): BadgeJpaEntity =
        throw UnsupportedOperationException("BadgeJpaEntity requires UserJpaEntity — use constructor directly")
}
