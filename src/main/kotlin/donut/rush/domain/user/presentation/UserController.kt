package donut.rush.domain.user.presentation

import donut.rush.domain.user.port.`in`.GetUserUseCase
import donut.rush.domain.user.port.`in`.UpdateUserUseCase
import donut.rush.domain.user.presentation.dto.request.UpdateUserRequest
import donut.rush.domain.user.presentation.dto.response.UserResponse
import donut.rush.global.common.CommonResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "User", description = "유저 API")
@RestController
@RequestMapping("/user")
class UserController(
    private val getUserUseCase: GetUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
) {
    @Operation(summary = "내 정보 조회", description = "현재 로그인한 유저의 프로필 정보를 조회합니다.")
    @GetMapping
    fun getUser(authentication: Authentication): CommonResponse<UserResponse> {
        val userId = authentication.principal as UUID
        return CommonResponse.ok(getUserUseCase.getUser(userId))
    }

    @Operation(summary = "닉네임 변경", description = "현재 로그인한 유저의 닉네임을 변경합니다.")
    @PutMapping
    fun updateUser(
        authentication: Authentication,
        @RequestBody request: UpdateUserRequest,
    ): CommonResponse<Nothing> {
        val userId = authentication.principal as UUID
        updateUserUseCase.updateNickname(userId, request)
        return CommonResponse.ok("닉네임이 변경되었습니다.")
    }
}
