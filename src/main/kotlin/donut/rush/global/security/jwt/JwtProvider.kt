package donut.rush.global.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.Date
import java.util.UUID
import javax.crypto.SecretKey

@Component
class JwtProvider(
    private val jwtProperties: JwtProperties,
) {
    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtProperties.secret.toByteArray())
    }

    fun generateAccessToken(userId: UUID): String =
        Jwts
            .builder()
            .subject(userId.toString())
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + jwtProperties.accessTokenExpiration))
            .signWith(key)
            .compact()

    fun generateRefreshToken(userId: UUID): String =
        Jwts
            .builder()
            .subject(userId.toString())
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + jwtProperties.refreshTokenExpiration))
            .signWith(key)
            .compact()

    fun validateToken(token: String): Boolean = runCatching { getClaims(token) }.isSuccess

    fun getUserId(token: String): UUID = UUID.fromString(getClaims(token).subject)

    private fun getClaims(token: String): Claims =
        Jwts
            .parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
}
