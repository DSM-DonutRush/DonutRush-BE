package donut.rush.infrastructure.googleoauth

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "google.oauth")
data class GoogleOAuthProperties(
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
    val tokenUrl: String = "https://oauth2.googleapis.com/token",
    val userInfoUrl: String = "https://www.googleapis.com/oauth2/v3/userinfo",
)
