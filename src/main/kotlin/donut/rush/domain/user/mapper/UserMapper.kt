package donut.rush.domain.user.mapper

import donut.rush.domain.user.domain.User
import donut.rush.domain.user.entity.UserJpaEntity
import donut.rush.global.base.GenericMapper
import org.springframework.stereotype.Component

@Component
class UserMapper : GenericMapper<User, UserJpaEntity> {
    override fun toDomain(entity: UserJpaEntity): User =
        User(
            id = entity.id,
            email = entity.email,
            nickname = entity.nickname,
            profileImage = entity.profileImage,
            createdAt = entity.createdAt,
        )

    override fun toEntity(domain: User): UserJpaEntity =
        UserJpaEntity(
            email = domain.email,
            nickname = domain.nickname,
            profileImage = domain.profileImage,
            createdAt = domain.createdAt,
        )
}
