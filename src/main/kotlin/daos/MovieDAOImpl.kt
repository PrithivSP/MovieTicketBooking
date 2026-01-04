package daos

import daos.interfaces.MovieDAO
import models.Movie

class MovieDAOImpl(): MovieDAO {
    override fun getAllMovies(): List<Movie> {
        return DataStore.movies.values.toList()
    }

    override fun getMovieById(movieId: String): Movie? {
        return DataStore.movies.get(movieId)
    }

}