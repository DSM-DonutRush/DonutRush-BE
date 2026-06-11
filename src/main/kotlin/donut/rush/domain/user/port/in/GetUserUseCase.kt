package donut.rush.domain.user.port.`in`

import donut.rush.domain.user.presentation.dto.response.UserResponse
import java.util.UUID

interface GetUserUseCase {
    fun getUser(userId: UUID): UserResponse
}
