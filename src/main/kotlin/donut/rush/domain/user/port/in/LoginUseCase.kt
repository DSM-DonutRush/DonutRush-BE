package donut.rush.domain.user.port.`in`

import donut.rush.global.security.auth.dto.TokenResponse

interface LoginUseCase {
    fun login(code: String): TokenResponse

    fun refresh(refreshToken: String): TokenResponse
}
