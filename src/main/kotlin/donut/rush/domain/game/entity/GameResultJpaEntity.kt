package donut.rush.domain.game.entity

import donut.rush.domain.game.enums.Character
import donut.rush.domain.game.enums.Difficulty
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
@Table(name = "game_results")
class GameResultJpaEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserJpaEntity,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val character: Character,
    @Column(name = "clear_time", nullable = false)
    val clearTime: Long,
    @Column(nullable = false)
    val success: Boolean,
    @Column(name = "penalty_count", nullable = false)
    val penaltyCount: Int,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val difficulty: Difficulty,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : BaseUUIDEntity()
