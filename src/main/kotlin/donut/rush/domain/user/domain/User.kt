package donut.rush.domain.user.domain

import java.time.LocalDateTime
import java.util.UUID

data class User(
    val id: UUID,
    val email: String,
    val nickname: String,
    val profileImage: String?,
    val createdAt: LocalDateTime,
)
