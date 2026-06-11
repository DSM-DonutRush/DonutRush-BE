package donut.rush.domain.multiplayer.port.`in`

import java.util.UUID

interface UpdateGaugeUseCase {
    fun updateGauge(
        userId: UUID,
        roomId: String,
        score: Int,
    )
}
