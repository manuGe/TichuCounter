package com.manuel.tichucounter.data


class GameRepository(private val gameDao: GameDao) {

    fun getGame(id: Long) =
        gameDao.get(id)

    fun getAllGames() = gameDao.getAll()

    fun createGame(game: Game) =
        gameDao.insert(game)

    fun setGame(game: Game) =
        gameDao.update(game)

    fun deleteGame(game: Game) =
        gameDao.delete(game)
}
