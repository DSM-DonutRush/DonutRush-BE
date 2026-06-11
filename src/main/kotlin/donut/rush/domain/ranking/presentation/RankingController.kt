package donut.rush.domain.ranking.presentation

import donut.rush.domain.ranking.enums.RankingType
import donut.rush.domain.ranking.port.`in`.GetRankingUseCase
import donut.rush.domain.ranking.presentation.dto.response.RankingResponse
import donut.rush.global.common.CommonResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Ranking", description = "랭킹 API")
@RestController
@RequestMapping("/ranking")
class RankingController(
    private val getRankingUseCase: GetRankingUseCase,
) {
    @Operation(summary = "랭킹 조회", description = "전체 또는 주간 랭킹을 조회합니다. type은 overall 또는 weekly를 사용합니다.")
    @GetMapping
    fun getRanking(
        @RequestParam(defaultValue = "overall") type: String,
    ): CommonResponse<List<RankingResponse>> = CommonResponse.ok(getRankingUseCase.getRanking(RankingType.from(type)))
}
