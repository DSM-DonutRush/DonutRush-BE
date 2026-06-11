package donut.rush.domain.multiplayer.presentation

import donut.rush.domain.multiplayer.service.GameRoomService
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class GameWebSocketEventListener(
    private val gameRoomService: GameRoomService,
) {
    @EventListener
    fun handleSessionDisconnect(event: SessionDisconnectEvent) {
        gameRoomService.handlePlayerDisconnect(event.sessionId)
    }
}
