package donut.rush.domain.multiplayer.service

import donut.rush.domain.game.enums.Difficulty
import donut.rush.domain.multiplayer.domain.PlayerState
import donut.rush.domain.multiplayer.port.`in`.JoinMatchUseCase
import donut.rush.domain.multiplayer.port.out.GameRoomPort
import donut.rush.domain.multiplayer.presentation.dto.MatchFoundResponse
import donut.rush.domain.multiplayer.presentation.dto.PlayerInfoDto
import donut.rush.domain.user.port.out.UserPort
import donut.rush.global.error.exception.DonutRushException
import donut.rush.global.error.exception.ErrorCode
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MatchingService(
    private val gameRoomPort: GameRoomPort,
    private val userPort: UserPort,
    private val messagingTemplate: SimpMessagingTemplate,
) : JoinMatchUseCase {
    override fun join(
        userId: UUID,
        difficulty: Difficulty,
        sessionId: String,
    ) {
        val user = userPort.findById(userId) ?: throw DonutRushException(ErrorCode.USER_NOT_FOUND)
        val player = PlayerState(userId = userId, nickname = user.nickname, sessionId = sessionId)
        gameRoomPort.registerSession(sessionId, userId)

        val room = gameRoomPort.joinQueue(player, difficulty) ?: return

        val playerInfos = room.players.values.map { PlayerInfoDto(it.userId.toString(), it.nickname) }
        room.players.keys.forEach { playerId ->
            messagingTemplate.convertAndSend(
                "/topic/match/$playerId",
                MatchFoundResponse(roomId = room.roomId, difficulty = room.difficulty, players = playerInfos),
            )
        }
    }

    override fun leave(userId: UUID) {
        gameRoomPort.leaveQueue(userId)
    }
}
