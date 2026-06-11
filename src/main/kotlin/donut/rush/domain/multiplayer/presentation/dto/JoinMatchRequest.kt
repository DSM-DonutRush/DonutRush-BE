package donut.rush.domain.multiplayer.presentation.dto

import donut.rush.domain.game.enums.Difficulty

data class JoinMatchRequest(
    val difficulty: Difficulty,
)
