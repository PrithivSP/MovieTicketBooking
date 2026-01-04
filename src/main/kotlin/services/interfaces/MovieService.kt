package services.interfaces

import models.Movie

interface MovieService {
    fun getAllMovies(): List<Movie>

    fun getMovieById(movieId: String): Movie?

    fun searchMovieByName(movieName: String): MutableList<Movie>

    fun getMoviesForTheater(movieIds: MutableSet<String>): MutableList<Movie>
}