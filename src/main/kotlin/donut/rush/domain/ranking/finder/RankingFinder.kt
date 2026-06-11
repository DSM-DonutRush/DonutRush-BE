package donut.rush.domain.ranking.finder

import com.querydsl.jpa.impl.JPAQueryFactory
import donut.rush.domain.game.entity.QGameResultJpaEntity.gameResultJpaEntity
import donut.rush.domain.user.entity.QUserJpaEntity.userJpaEntity
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalDate

@Component
class RankingFinder(
    private val jpaQueryFactory: JPAQueryFactory,
) {
    fun findOverallRanking(): List<RankingResult> =
        jpaQueryFactory
            .select(
                userJpaEntity.id,
                userJpaEntity.nickname,
                gameResultJpaEntity.clearTime,
                gameResultJpaEntity.character,
                gameResultJpaEntity.createdAt,
            ).from(gameResultJpaEntity)
            .join(gameResultJpaEntity.user, userJpaEntity)
            .where(gameResultJpaEntity.success.isTrue)
            .orderBy(gameResultJpaEntity.clearTime.asc(), gameResultJpaEntity.createdAt.desc())
            .fetch()
            .map { tuple ->
                RankingResult(
                    userId = tuple[userJpaEntity.id]!!,
                    nickname = tuple[userJpaEntity.nickname]!!,
                    clearTime = tuple[gameResultJpaEntity.clearTime]!!,
                    character = tuple[gameResultJpaEntity.character]!!,
                    createdAt = tuple[gameResultJpaEntity.createdAt]!!,
                )
            }

    fun findWeeklyRanking(): List<RankingResult> {
        val mondayStart = LocalDate.now().with(DayOfWeek.MONDAY).atStartOfDay()

        return jpaQueryFactory
            .select(
                userJpaEntity.id,
                userJpaEntity.nickname,
                gameResultJpaEntity.clearTime,
                gameResultJpaEntity.character,
                gameResultJpaEntity.createdAt,
            ).from(gameResultJpaEntity)
            .join(gameResultJpaEntity.user, userJpaEntity)
            .where(
                gameResultJpaEntity.success.isTrue,
                gameResultJpaEntity.createdAt.goe(mondayStart),
            ).orderBy(gameResultJpaEntity.clearTime.asc(), gameResultJpaEntity.createdAt.desc())
            .fetch()
            .map { tuple ->
                RankingResult(
                    userId = tuple[userJpaEntity.id]!!,
                    nickname = tuple[userJpaEntity.nickname]!!,
                    clearTime = tuple[gameResultJpaEntity.clearTime]!!,
                    character = tuple[gameResultJpaEntity.character]!!,
                    createdAt = tuple[gameResultJpaEntity.createdAt]!!,
                )
            }
    }
}
