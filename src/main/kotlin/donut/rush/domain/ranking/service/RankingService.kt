package donut.rush.domain.ranking.service

import donut.rush.domain.badge.port.out.BadgePort
import donut.rush.domain.ranking.enums.RankingType
import donut.rush.domain.ranking.finder.RankingFinder
import donut.rush.domain.ranking.port.`in`.GetRankingUseCase
import donut.rush.domain.ranking.presentation.dto.response.RankingResponse
import org.springframework.stereotype.Service

@Service
class RankingService(
    private val rankingFinder: RankingFinder,
    private val badgePort: BadgePort,
) : GetRankingUseCase {
    override fun getRanking(type: RankingType): List<RankingResponse> {
        val results =
            when (type) {
                RankingType.OVERALL -> rankingFinder.findOverallRanking()
                RankingType.WEEKLY -> rankingFinder.findWeeklyRanking()
            }

        val userIds = results.map { it.userId }.distinct()
        val badgesByUser =
            badgePort
                .findByUserIdIn(userIds)
                .groupBy { it.userId }

        return results.mapIndexed { index, result ->
            val badges = badgesByUser[result.userId].orEmpty()
            RankingResponse(
                rank = index + 1,
                nickname = result.nickname,
                clearTime = result.clearTime,
                character = result.character,
                badges = badges.map { it.badgeType.displayName },
            )
        }
    }
}
