package donut.rush.global.security.auth.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
