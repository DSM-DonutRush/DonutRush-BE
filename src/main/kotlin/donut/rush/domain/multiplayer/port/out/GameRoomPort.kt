package donut.rush.domain.multiplayer.port.out

import donut.rush.domain.game.enums.Difficulty
import donut.rush.domain.multiplayer.domain.GameRoom
import donut.rush.domain.multiplayer.domain.PlayerState
import java.util.UUID

interface GameRoomPort {
    fun joinQueue(
        player: PlayerState,
        difficulty: Difficulty,
    ): GameRoom?

    fun leaveQueue(userId: UUID)

    fun findRoomById(roomId: String): GameRoom?

    fun findRoomByUserId(userId: UUID): GameRoom?

    fun findRoomBySessionId(sessionId: String): GameRoom?

    fun removeRoom(roomId: String)

    fun registerSession(
        sessionId: String,
        userId: UUID,
    )

    fun unregisterSession(sessionId: String): UUID?
}
