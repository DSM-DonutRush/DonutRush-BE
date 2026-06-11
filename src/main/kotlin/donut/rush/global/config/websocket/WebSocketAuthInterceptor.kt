package donut.rush.global.config.websocket

import donut.rush.global.security.jwt.JwtProvider
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.stereotype.Component
import java.security.Principal

@Component
class WebSocketAuthInterceptor(
    private val jwtProvider: JwtProvider,
) : ChannelInterceptor {
    override fun preSend(
        message: Message<*>,
        channel: MessageChannel,
    ): Message<*>? {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)

        if (accessor?.command == StompCommand.CONNECT) {
            val token = accessor.getFirstNativeHeader("Authorization")?.removePrefix("Bearer ")
            if (token != null && jwtProvider.validateToken(token)) {
                val userId = jwtProvider.getUserId(token)
                accessor.user = Principal { userId.toString() }
            }
        }

        return message
    }
}
