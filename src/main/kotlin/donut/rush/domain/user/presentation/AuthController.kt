package donut.rush.domain.user.presentation

import donut.rush.domain.user.port.`in`.LoginUseCase
import donut.rush.global.common.CommonResponse
import donut.rush.global.security.auth.dto.LoginRequest
import donut.rush.global.security.auth.dto.RefreshRequest
import donut.rush.global.security.auth.dto.TokenResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/auth")
class AuthController(
    private val loginUseCase: LoginUseCase,
) {
    @Operation(summary = "Google OAuth 로그인", description = "Google OAuth authorization code로 JWT 토큰을 발급합니다.")
    @PostMapping("/google")
    fun login(
        @RequestBody request: LoginRequest,
    ): CommonResponse<TokenResponse> = CommonResponse.ok(loginUseCase.login(request.code))

    @Operation(summary = "토큰 재발급", description = "Refresh token으로 access token과 refresh token을 재발급합니다.")
    @PostMapping("/refresh")
    fun refresh(
        @RequestBody request: RefreshRequest,
    ): CommonResponse<TokenResponse> = CommonResponse.ok(loginUseCase.refresh(request.refreshToken))
}
