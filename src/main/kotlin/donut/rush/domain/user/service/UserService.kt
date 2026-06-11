package donut.rush.domain.user.service

import donut.rush.domain.user.port.`in`.GetUserUseCase
import donut.rush.domain.user.port.`in`.UpdateUserUseCase
import donut.rush.domain.user.port.out.UserPort
import donut.rush.domain.user.presentation.dto.request.UpdateUserRequest
import donut.rush.domain.user.presentation.dto.response.UserResponse
import donut.rush.global.error.exception.DonutRushException
import donut.rush.global.error.exception.ErrorCode
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userPort: UserPort,
) : GetUserUseCase,
    UpdateUserUseCase {
    override fun getUser(userId: UUID): UserResponse {
        val user = userPort.findById(userId) ?: throw DonutRushException(ErrorCode.USER_NOT_FOUND)
        return UserResponse.from(user)
    }

    override fun updateNickname(
        userId: UUID,
        request: UpdateUserRequest,
    ) {
        if (userPort.existsByNickname(request.nickname)) {
            throw DonutRushException(ErrorCode.DUPLICATE_NICKNAME)
        }
        val user = userPort.findById(userId) ?: throw DonutRushException(ErrorCode.USER_NOT_FOUND)
        userPort.save(user.copy(nickname = request.nickname))
    }
}
