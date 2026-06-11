package donut.rush.domain.ranking.port.`in`

import donut.rush.domain.ranking.enums.RankingType
import donut.rush.domain.ranking.presentation.dto.response.RankingResponse

interface GetRankingUseCase {
    fun getRanking(type: RankingType): List<RankingResponse>
}
