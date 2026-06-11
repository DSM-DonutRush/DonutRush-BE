package donut.rush.domain.user.persistence

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.util.concurrent.TimeUnit

@Repository
class RefreshTokenRepository(
    private val redisTemplate: StringRedisTemplate,
) {
    companion object {
        private const val KEY_PREFIX = "refresh:"
        private const val TTL_DAYS = 14L
    }

    fun save(
        userId: String,
        refreshToken: String,
    ) {
        redisTemplate.opsForValue().set("$KEY_PREFIX$userId", refreshToken, TTL_DAYS, TimeUnit.DAYS)
    }

    fun findByUserId(userId: String): String? = redisTemplate.opsForValue().get("$KEY_PREFIX$userId")

    fun delete(userId: String) {
        redisTemplate.delete("$KEY_PREFIX$userId")
    }
}
