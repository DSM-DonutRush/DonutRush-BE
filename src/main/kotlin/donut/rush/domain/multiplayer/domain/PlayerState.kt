package donut.rush.domain.multiplayer.domain

import java.util.UUID

class PlayerState(
    val userId: UUID,
    val nickname: String,
    val sessionId: String,
    @Volatile var score: Int = 0,
)
