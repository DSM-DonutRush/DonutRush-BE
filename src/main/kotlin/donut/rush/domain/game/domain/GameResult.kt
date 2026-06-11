package donut.rush.domain.game.domain

import donut.rush.domain.game.enums.Character
import donut.rush.domain.game.enums.Difficulty
import java.time.LocalDateTime
import java.util.UUID

data class GameResult(
    val id: UUID,
    val userId: UUID,
    val character: Character,
    val clearTime: Long,
    val success: Boolean,
    val penaltyCount: Int,
    val difficulty: Difficulty,
    val createdAt: LocalDateTime,
)
