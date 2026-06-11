package donut.rush.domain.multiplayer.infrastructure

import donut.rush.domain.game.enums.Difficulty
import donut.rush.domain.multiplayer.domain.GameRoom
import donut.rush.domain.multiplayer.domain.PlayerState
import donut.rush.domain.multiplayer.port.out.GameRoomPort
import org.springframework.stereotype.Component
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryGameRoomStore : GameRoomPort {
    private val rooms = ConcurrentHashMap<String, GameRoom>()
    private val userRoomMap = ConcurrentHashMap<UUID, String>()
    private val sessionUserMap = ConcurrentHashMap<String, UUID>()
    private val waitingQueues = ConcurrentHashMap<Difficulty, MutableList<PlayerState>>()

    @Synchronized
    override fun joinQueue(
        player: PlayerState,
        difficulty: Difficulty,
    ): GameRoom? {
        val queue = waitingQueues.getOrPut(difficulty) { mutableListOf() }
        queue.add(player)

        return if (queue.size >= 2) {
            val matched = queue.take(minOf(queue.size, 4))
            queue.removeAll(matched.toSet())
            createRoom(matched, difficulty)
        } else {
            null
        }
    }

    @Synchronized
    override fun leaveQueue(userId: UUID) {
        waitingQueues.values.forEach { queue ->
            queue.removeIf { it.userId == userId }
        }
    }

    override fun findRoomById(roomId: String): GameRoom? = rooms[roomId]

    override fun findRoomByUserId(userId: UUID): GameRoom? = userRoomMap[userId]?.let { rooms[it] }

    override fun findRoomBySessionId(sessionId: String): GameRoom? = sessionUserMap[sessionId]?.let { findRoomByUserId(it) }

    override fun removeRoom(roomId: String) {
        rooms[roomId]?.players?.keys?.forEach { userRoomMap.remove(it) }
        rooms.remove(roomId)
    }

    override fun registerSession(
        sessionId: String,
        userId: UUID,
    ) {
        sessionUserMap[sessionId] = userId
    }

    override fun unregisterSession(sessionId: String): UUID? {
        val userId = sessionUserMap.remove(sessionId)
        if (userId != null) userRoomMap.remove(userId)
        return userId
    }

    private fun createRoom(
        players: List<PlayerState>,
        difficulty: Difficulty,
    ): GameRoom {
        val room =
            GameRoom(
                roomId = UUID.randomUUID().toString(),
                difficulty = difficulty,
            )
        players.forEach { player ->
            room.players[player.userId] = player
            userRoomMap[player.userId] = room.roomId
            sessionUserMap[player.sessionId] = player.userId
        }
        rooms[room.roomId] = room
        return room
    }
}
