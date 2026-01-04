package daos.interfaces

import daos.DataStore
import models.Screen
import models.Show

interface ShowDAO {
    fun getShowById(showId: String): Show?

    fun getShowsByTheaterId(theaterId: String): List<Show>

    fun getShowsByScreenId(screenId: String): List<Show>

    fun getShowsByMovieId(movieId: String): List<Show>

    fun getShowsByMovieAndTheater(
        movieId: String,
        theaterId: String
    ): List<Show>

    fun getShowByMovieTheaterScreen(
        movieId: String,
        theaterId: String,
        screenId: String
    ): List<Show>
}