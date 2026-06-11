package donut.rush.domain.multiplayer.domain

import donut.rush.domain.game.enums.Difficulty
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class GameRoom(
    val roomId: String,
    val difficulty: Difficulty,
    val players: ConcurrentHashMap<UUID, PlayerState> = ConcurrentHashMap(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
