package donut.rush.domain.game.mapper

import donut.rush.domain.game.domain.GameResult
import donut.rush.domain.game.entity.GameResultJpaEntity
import donut.rush.global.base.GenericMapper
import org.springframework.stereotype.Component

@Component
class GameResultMapper : GenericMapper<GameResult, GameResultJpaEntity> {
    override fun toDomain(entity: GameResultJpaEntity): GameResult =
        GameResult(
            id = entity.id,
            userId = entity.user.id,
            character = entity.character,
            clearTime = entity.clearTime,
            success = entity.success,
            penaltyCount = entity.penaltyCount,
            difficulty = entity.difficulty,
            createdAt = entity.createdAt,
        )

    override fun toEntity(domain: GameResult): GameResultJpaEntity =
        throw UnsupportedOperationException("GameResultJpaEntity requires UserJpaEntity — use constructor directly")
}
