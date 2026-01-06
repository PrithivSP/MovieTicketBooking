package services.interfaces

import models.Movie
import models.enumerations.Genre

interface MovieService {
    fun getAllMovies(): List<Movie>

    fun getMovieById(movieId: String): Movie?

    fun searchMovieByName(movieName: String): MutableList<Movie>

    fun getMoviesForTheater(movieIds: MutableSet<String>): MutableList<Movie>

    fun getMoviesForGenre(chosenGenre: Genre): List<Movie>
}