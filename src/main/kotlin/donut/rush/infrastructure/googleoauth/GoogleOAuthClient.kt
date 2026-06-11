package donut.rush.infrastructure.googleoauth

import donut.rush.infrastructure.googleoauth.dto.GoogleUserInfoResponse

interface GoogleOAuthClient {
    fun getAccessToken(code: String): String

    fun getUserInfo(accessToken: String): GoogleUserInfoResponse
}
