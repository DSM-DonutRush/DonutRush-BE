package donut.rush.domain.game.port.out

import donut.rush.domain.game.domain.GameResult

interface GameResultPort {
    fun save(gameResult: GameResult): GameResult
}
