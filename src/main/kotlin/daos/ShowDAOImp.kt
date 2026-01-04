package daos

import daos.interfaces.ShowDAO
import models.Show

class ShowDAOImp : ShowDAO {
    override fun getShowById(showId: String): Show? {
        return DataStore.shows[showId]
    }

    override fun getShowsByTheaterId(theaterId: String): List<Show> {
        return DataStore.shows.values.filter { show ->
            show.theaterId.equals(theaterId, true)
        }
    }

    override fun getShowsByScreenId(screenId: String): List<Show> {
        return DataStore.shows.values.filter { show ->
            show.screenId.equals(screenId, true)
        }
    }

    override fun getShowsByMovieId(movieId: String): List<Show> {
        return DataStore.shows.values.filter { show ->
            show.movieId.equals(movieId, true)
        }
    }

    override fun getShowsByMovieAndTheater(
        movieId: String,
        theaterId: String
    ): List<Show> {
        return DataStore.shows.values.filter { show ->
            show.theaterId.equals(theaterId, true) && show.movieId.equals(movieId, true)
        }
    }

    override fun getShowByMovieTheaterScreen(
        movieId: String,
        theaterId: String,
        screenId: String
    ): List<Show> {
        return DataStore.shows.values.filter { show ->
            show.movieId.equals(movieId, true) &&
                    show.theaterId.equals(theaterId, true) &&
                    show.screenId.equals(screenId, true)
        }
    }
}