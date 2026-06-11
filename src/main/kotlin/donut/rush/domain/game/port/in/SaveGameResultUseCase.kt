package donut.rush.domain.game.port.`in`

import donut.rush.domain.game.presentation.dto.request.SaveGameResultRequest
import donut.rush.domain.game.presentation.dto.response.SaveGameResultResponse
import java.util.UUID

interface SaveGameResultUseCase {
    fun save(
        userId: UUID,
        request: SaveGameResultRequest,
    ): SaveGameResultResponse
}
