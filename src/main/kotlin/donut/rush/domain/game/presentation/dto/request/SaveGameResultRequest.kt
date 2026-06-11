package donut.rush.domain.game.presentation.dto.request

import donut.rush.domain.game.enums.Character
import donut.rush.domain.game.enums.Difficulty

data class SaveGameResultRequest(
    val character: Character,
    val clearTime: Long,
    val success: Boolean,
    val penaltyCount: Int,
    val difficulty: Difficulty,
)
