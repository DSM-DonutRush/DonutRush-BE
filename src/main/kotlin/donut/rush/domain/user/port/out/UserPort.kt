package donut.rush.domain.user.port.out

import donut.rush.domain.user.domain.User
import java.util.UUID

interface UserPort {
    fun findByEmail(email: String): User?

    fun findById(id: UUID): User?

    fun save(user: User): User

    fun existsByNickname(nickname: String): Boolean
}
