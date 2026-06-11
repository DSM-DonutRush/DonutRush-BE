package donut.rush.domain.multiplayer.presentation.dto

import donut.rush.domain.game.enums.Difficulty
import donut.rush.domain.multiplayer.enums.GameMessageType

data class MatchFoundResponse(
    val type: GameMessageType = GameMessageType.MATCH_FOUND,
    val roomId: String,
    val difficulty: Difficulty,
    val players: List<PlayerInfoDto>,
)

data class PlayerInfoDto(
    val userId: String,
    val nickname: String,
)
