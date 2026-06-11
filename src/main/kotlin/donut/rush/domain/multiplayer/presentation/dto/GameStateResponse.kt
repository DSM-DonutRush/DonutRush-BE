package donut.rush.domain.multiplayer.presentation.dto

import donut.rush.domain.multiplayer.enums.GameMessageType

data class GameStateResponse(
    val type: GameMessageType,
    val players: List<PlayerScoreDto>,
    val winnerId: String? = null,
)

data class PlayerScoreDto(
    val userId: String,
    val nickname: String,
    val score: Int,
)
