package donut.rush.domain.user.service

import donut.rush.domain.user.domain.User
import donut.rush.domain.user.persistence.RefreshTokenRepository
import donut.rush.domain.user.port.`in`.LoginUseCase
import donut.rush.domain.user.port.out.UserPort
import donut.rush.global.error.exception.DonutRushException
import donut.rush.global.error.exception.ErrorCode
import donut.rush.global.security.auth.dto.TokenResponse
import donut.rush.global.security.jwt.JwtProvider
import donut.rush.infrastructure.googleoauth.GoogleOAuthClient
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class AuthService(
    private val googleOAuthClient: GoogleOAuthClient,
    private val userPort: UserPort,
    private val jwtProvider: JwtProvider,
    private val refreshTokenRepository: RefreshTokenRepository,
) : LoginUseCase {
    override fun login(code: String): TokenResponse {
        val googleAccessToken = googleOAuthClient.getAccessToken(code)
        val userInfo = googleOAuthClient.getUserInfo(googleAccessToken)

        val user =
            userPort.findByEmail(userInfo.email) ?: userPort.save(
                User(
                    id = UUID.randomUUID(),
                    email = userInfo.email,
                    nickname = userInfo.name,
                    profileImage = userInfo.picture,
                    createdAt = LocalDateTime.now(),
                ),
            )

        return issueTokens(user)
    }

    override fun refresh(refreshToken: String): TokenResponse {
        if (!jwtProvider.validateToken(refreshToken)) {
            throw DonutRushException(ErrorCode.INVALID_TOKEN)
        }

        val userId = jwtProvider.getUserId(refreshToken)
        val stored =
            refreshTokenRepository.findByUserId(userId.toString())
                ?: throw DonutRushException(ErrorCode.INVALID_TOKEN)

        if (stored != refreshToken) throw DonutRushException(ErrorCode.INVALID_TOKEN)

        val user = userPort.findById(userId) ?: throw DonutRushException(ErrorCode.USER_NOT_FOUND)
        return issueTokens(user)
    }

    private fun issueTokens(user: User): TokenResponse {
        val accessToken = jwtProvider.generateAccessToken(user.id)
        val newRefreshToken = jwtProvider.generateRefreshToken(user.id)
        refreshTokenRepository.save(user.id.toString(), newRefreshToken)
        return TokenResponse(accessToken, newRefreshToken)
    }
}
