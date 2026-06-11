package donut.rush.domain.ranking.presentation.dto.response

import donut.rush.domain.game.enums.Character

data class RankingResponse(
    val rank: Int,
    val nickname: String,
    val clearTime: Long,
    val character: Character,
    val badges: List<String>,
)
