package donut.rush.domain.game.presentation

import donut.rush.domain.game.port.`in`.SaveGameResultUseCase
import donut.rush.domain.game.presentation.dto.request.SaveGameResultRequest
import donut.rush.domain.game.presentation.dto.response.SaveGameResultResponse
import donut.rush.global.common.CommonResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "Game", description = "게임 결과 API")
@RestController
@RequestMapping("/game")
class GameController(
    private val saveGameResultUseCase: SaveGameResultUseCase,
) {
    @Operation(summary = "게임 결과 저장", description = "게임 종료 후 결과를 저장하고 새로 획득한 뱃지를 반환합니다.")
    @PostMapping("/result")
    fun saveResult(
        authentication: Authentication,
        @RequestBody request: SaveGameResultRequest,
    ): CommonResponse<SaveGameResultResponse> {
        val userId = authentication.principal as UUID
        return CommonResponse.ok(saveGameResultUseCase.save(userId, request), "결과 저장 완료")
    }
}
