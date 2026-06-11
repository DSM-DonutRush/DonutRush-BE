package donut.rush.domain.user.entity

import donut.rush.global.base.BaseUUIDEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class UserJpaEntity(
    @Column(nullable = false, unique = true)
    val email: String,
    @Column(nullable = false, unique = true)
    var nickname: String,
    @Column(name = "profile_image")
    var profileImage: String? = null,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : BaseUUIDEntity()
