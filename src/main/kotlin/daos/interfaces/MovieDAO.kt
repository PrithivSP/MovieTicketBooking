package daos.interfaces

import models.Movie

interface MovieDAO {

    fun getAllMovies(): List<Movie>
    fun getMovieById(movieId: String): Movie?

}