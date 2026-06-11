package donut.rush.domain.badge.entity

import donut.rush.domain.badge.enums.BadgeType
import donut.rush.domain.user.entity.UserJpaEntity
import donut.rush.global.base.BaseUUIDEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "badges")
class BadgeJpaEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserJpaEntity,
    @Enumerated(EnumType.STRING)
    @Column(name = "badge_type", nullable = false)
    val badgeType: BadgeType,
    @Column(name = "earned_at", nullable = false, updatable = false)
    val earnedAt: LocalDateTime = LocalDateTime.now(),
) : BaseUUIDEntity()
