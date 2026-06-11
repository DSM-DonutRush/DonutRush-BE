package donut.rush.infrastructure.googleoauth.dto

data class GoogleUserInfoResponse(
    val email: String,
    val name: String,
    val picture: String?,
)
