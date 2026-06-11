package donut.rush.domain.user.persistence

import donut.rush.domain.user.domain.User
import donut.rush.domain.user.entity.UserJpaEntity
import donut.rush.domain.user.mapper.UserMapper
import donut.rush.domain.user.persistence.repository.UserRepository
import donut.rush.domain.user.port.out.UserPort
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UserPersistenceAdapter(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) : UserPort {
    override fun findByEmail(email: String): User? = userRepository.findByEmail(email)?.let { userMapper.toDomain(it) }

    override fun findById(id: UUID): User? = userRepository.findById(id).orElse(null)?.let { userMapper.toDomain(it) }

    override fun save(user: User): User {
        val entity =
            userRepository
                .findById(user.id)
                .map { existing ->
                    existing.nickname = user.nickname
                    existing.profileImage = user.profileImage
                    existing
                }.orElseGet {
                    UserJpaEntity(
                        email = user.email,
                        nickname = user.nickname,
                        profileImage = user.profileImage,
                        createdAt = user.createdAt,
                    )
                }
        return userMapper.toDomain(userRepository.save(entity))
    }

    override fun existsByNickname(nickname: String): Boolean = userRepository.existsByNickname(nickname)
}
