package donut.rush.domain.user.port.`in`

import donut.rush.domain.user.presentation.dto.request.UpdateUserRequest
import java.util.UUID

interface UpdateUserUseCase {
    fun updateNickname(
        userId: UUID,
        request: UpdateUserRequest,
    )
}
