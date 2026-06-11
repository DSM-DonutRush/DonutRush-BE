package donut.rush.infrastructure.googleoauth

import donut.rush.global.error.exception.DonutRushException
import donut.rush.global.error.exception.ErrorCode
import donut.rush.infrastructure.googleoauth.dto.GoogleTokenResponse
import donut.rush.infrastructure.googleoauth.dto.GoogleUserInfoResponse
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class GoogleOAuthClientImpl(
    restClientBuilder: RestClient.Builder,
    private val properties: GoogleOAuthProperties,
) : GoogleOAuthClient {
    private val restClient = restClientBuilder.build()

    override fun getAccessToken(code: String): String {
        val body =
            "grant_type=authorization_code" +
                "&code=$code" +
                "&client_id=${properties.clientId}" +
                "&client_secret=${properties.clientSecret}" +
                "&redirect_uri=${properties.redirectUri}"

        val response =
            restClient
                .post()
                .uri(properties.tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(GoogleTokenResponse::class.java)
                ?: throw DonutRushException(ErrorCode.INVALID_TOKEN)

        return response.accessToken
    }

    override fun getUserInfo(accessToken: String): GoogleUserInfoResponse =
        restClient
            .get()
            .uri(properties.userInfoUrl)
            .header("Authorization", "Bearer $accessToken")
            .retrieve()
            .body(GoogleUserInfoResponse::class.java)
            ?: throw DonutRushException(ErrorCode.INVALID_TOKEN)
}
