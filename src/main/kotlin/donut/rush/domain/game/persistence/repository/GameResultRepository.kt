package donut.rush.domain.game.persistence.repository

import donut.rush.domain.game.entity.GameResultJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface GameResultRepository : JpaRepository<GameResultJpaEntity, UUID>
