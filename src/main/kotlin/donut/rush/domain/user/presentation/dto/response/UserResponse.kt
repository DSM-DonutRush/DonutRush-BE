package donut.rush.domain.user.presentation.dto.response

import donut.rush.domain.user.domain.User
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val nickname: String,
    val profileImage: String?,
) {
    companion object {
        fun from(user: User) =
            UserResponse(
                id = user.id,
                nickname = user.nickname,
                profileImage = user.profileImage,
            )
    }
}
