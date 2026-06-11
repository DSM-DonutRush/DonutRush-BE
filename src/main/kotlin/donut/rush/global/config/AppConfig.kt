package donut.rush.global.config

import donut.rush.global.security.jwt.JwtProperties
import donut.rush.infrastructure.googleoauth.GoogleOAuthProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
@EnableConfigurationProperties(JwtProperties::class, GoogleOAuthProperties::class)
class AppConfig {
    @Bean
    fun restClientBuilder(): RestClient.Builder = RestClient.builder()
}
