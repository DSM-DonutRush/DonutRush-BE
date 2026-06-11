package donut.rush.domain.game.persistence

import donut.rush.domain.game.domain.GameResult
import donut.rush.domain.game.entity.GameResultJpaEntity
import donut.rush.domain.game.mapper.GameResultMapper
import donut.rush.domain.game.persistence.repository.GameResultRepository
import donut.rush.domain.game.port.out.GameResultPort
import donut.rush.domain.user.persistence.repository.UserRepository
import donut.rush.global.error.exception.DonutRushException
import donut.rush.global.error.exception.ErrorCode
import org.springframework.stereotype.Component

@Component
class GameResultPersistenceAdapter(
    private val gameResultRepository: GameResultRepository,
    private val userRepository: UserRepository,
    private val gameResultMapper: GameResultMapper,
) : GameResultPort {
    override fun save(gameResult: GameResult): GameResult {
        val user =
            userRepository
                .findById(gameResult.userId)
                .orElseThrow { DonutRushException(ErrorCode.USER_NOT_FOUND) }
        val entity =
            GameResultJpaEntity(
                user = user,
                character = gameResult.character,
                clearTime = gameResult.clearTime,
                success = gameResult.success,
                penaltyCount = gameResult.penaltyCount,
                difficulty = gameResult.difficulty,
                createdAt = gameResult.createdAt,
            )
        return gameResultMapper.toDomain(gameResultRepository.save(entity))
    }
}
