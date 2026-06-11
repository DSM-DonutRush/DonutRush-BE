package donut.rush.domain.ranking.finder

import donut.rush.domain.game.enums.Character
import java.time.LocalDateTime
import java.util.UUID

data class RankingResult(
    val userId: UUID,
    val nickname: String,
    val clearTime: Long,
    val character: Character,
    val createdAt: LocalDateTime,
)
