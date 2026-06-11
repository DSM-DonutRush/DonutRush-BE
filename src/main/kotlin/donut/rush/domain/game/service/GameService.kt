package donut.rush.domain.game.service

import donut.rush.domain.badge.port.`in`.EarnBadgeUseCase
import donut.rush.domain.game.domain.GameResult
import donut.rush.domain.game.port.`in`.SaveGameResultUseCase
import donut.rush.domain.game.port.out.GameResultPort
import donut.rush.domain.game.presentation.dto.request.SaveGameResultRequest
import donut.rush.domain.game.presentation.dto.response.SaveGameResultResponse
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class GameService(
    private val gameResultPort: GameResultPort,
    private val earnBadgeUseCase: EarnBadgeUseCase,
) : SaveGameResultUseCase {
    override fun save(
        userId: UUID,
        request: SaveGameResultRequest,
    ): SaveGameResultResponse {
        val gameResult =
            GameResult(
                id = UUID.randomUUID(),
                userId = userId,
                character = request.character,
                clearTime = request.clearTime,
                success = request.success,
                penaltyCount = request.penaltyCount,
                difficulty = request.difficulty,
                createdAt = LocalDateTime.now(),
            )

        gameResultPort.save(gameResult)

        val earnedBadges =
            if (gameResult.success) {
                earnBadgeUseCase.earnBadges(userId, gameResult)
            } else {
                emptyList()
            }

        return SaveGameResultResponse(badgesEarned = earnedBadges.map { it.displayName })
    }
}
