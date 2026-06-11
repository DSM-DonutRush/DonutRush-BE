package donut.rush.domain.multiplayer.port.`in`

import donut.rush.domain.game.enums.Difficulty
import java.util.UUID

interface JoinMatchUseCase {
    fun join(
        userId: UUID,
        difficulty: Difficulty,
        sessionId: String,
    )

    fun leave(userId: UUID)
}
