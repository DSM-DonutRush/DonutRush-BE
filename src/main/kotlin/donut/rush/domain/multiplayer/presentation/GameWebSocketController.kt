package donut.rush.domain.multiplayer.presentation

import donut.rush.domain.multiplayer.port.`in`.JoinMatchUseCase
import donut.rush.domain.multiplayer.port.`in`.UpdateGaugeUseCase
import donut.rush.domain.multiplayer.presentation.dto.GaugeUpdateRequest
import donut.rush.domain.multiplayer.presentation.dto.JoinMatchRequest
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller
import java.security.Principal
import java.util.UUID

@Controller
class GameWebSocketController(
    private val joinMatchUseCase: JoinMatchUseCase,
    private val updateGaugeUseCase: UpdateGaugeUseCase,
) {
    @MessageMapping("/match/join")
    fun joinMatch(
        request: JoinMatchRequest,
        principal: Principal,
        headerAccessor: SimpMessageHeaderAccessor,
    ) {
        val sessionId = headerAccessor.sessionId ?: return
        joinMatchUseCase.join(UUID.fromString(principal.name), request.difficulty, sessionId)
    }

    @MessageMapping("/match/leave")
    fun leaveMatch(principal: Principal) {
        joinMatchUseCase.leave(UUID.fromString(principal.name))
    }

    @MessageMapping("/game/{roomId}/gauge")
    fun updateGauge(
        @DestinationVariable roomId: String,
        request: GaugeUpdateRequest,
        principal: Principal,
    ) {
        updateGaugeUseCase.updateGauge(UUID.fromString(principal.name), roomId, request.score)
    }
}
