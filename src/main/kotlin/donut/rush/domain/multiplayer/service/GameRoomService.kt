package donut.rush.domain.multiplayer.service

import donut.rush.domain.multiplayer.enums.GameMessageType
import donut.rush.domain.multiplayer.port.`in`.UpdateGaugeUseCase
import donut.rush.domain.multiplayer.port.out.GameRoomPort
import donut.rush.domain.multiplayer.presentation.dto.GameStateResponse
import donut.rush.domain.multiplayer.presentation.dto.PlayerScoreDto
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GameRoomService(
    private val gameRoomPort: GameRoomPort,
    private val messagingTemplate: SimpMessagingTemplate,
) : UpdateGaugeUseCase {
    companion object {
        const val CLEAR_SCORE = 90
    }

    override fun updateGauge(
        userId: UUID,
        roomId: String,
        score: Int,
    ) {
        val room = gameRoomPort.findRoomById(roomId) ?: return
        val player = room.players[userId] ?: return

        player.score = score

        val playerScores = room.players.values.map { PlayerScoreDto(it.userId.toString(), it.nickname, it.score) }

        if (score >= CLEAR_SCORE) {
            messagingTemplate.convertAndSend(
                "/topic/game/$roomId",
                GameStateResponse(type = GameMessageType.GAME_OVER, players = playerScores, winnerId = userId.toString()),
            )
            gameRoomPort.removeRoom(roomId)
        } else {
            messagingTemplate.convertAndSend(
                "/topic/game/$roomId",
                GameStateResponse(type = GameMessageType.GAUGE_UPDATE, players = playerScores),
            )
        }
    }

    fun handlePlayerDisconnect(sessionId: String) {
        val room =
            gameRoomPort.findRoomBySessionId(sessionId) ?: run {
                gameRoomPort.unregisterSession(sessionId)
                return
            }
        val userId = gameRoomPort.unregisterSession(sessionId)
        if (userId != null) room.players.remove(userId)

        if (room.players.size <= 1) {
            val remaining = room.players.values.firstOrNull()
            val playerScores = room.players.values.map { PlayerScoreDto(it.userId.toString(), it.nickname, it.score) }
            messagingTemplate.convertAndSend(
                "/topic/game/${room.roomId}",
                GameStateResponse(
                    type = GameMessageType.GAME_OVER,
                    players = playerScores,
                    winnerId = remaining?.userId?.toString(),
                ),
            )
            gameRoomPort.removeRoom(room.roomId)
        } else {
            val playerScores = room.players.values.map { PlayerScoreDto(it.userId.toString(), it.nickname, it.score) }
            messagingTemplate.convertAndSend(
                "/topic/game/${room.roomId}",
                GameStateResponse(type = GameMessageType.PLAYER_LEFT, players = playerScores),
            )
        }
    }
}
