package services

import daos.MovieDAOImpl
import daos.interfaces.MovieDAO
import models.Movie
import services.interfaces.MovieService


class MovieServiceImp(val movieDAO: MovieDAO = MovieDAOImpl()): MovieService {
    override fun getAllMovies(): List<Movie> {
        return movieDAO.getAllMovies()
    }

    override fun getMovieById(movieId: String): Movie? {
        return movieDAO.getMovieById(movieId)
    }

    override fun searchMovieByName(movieName: String): MutableList<Movie> {
        val key = movieName.trim()
        return movieDAO.getAllMovies()
            .filter { it.movieName.contains(key, ignoreCase = true) }
            .toMutableList()
    }

    override fun getMoviesForTheater(movieIds: MutableSet<String>): MutableList<Movie> {
        val movieList = mutableListOf<Movie>()

        movieIds.forEach { id ->
            movieDAO.getMovieById(id)?.let {
                movieList.add(it)
            }
        }

        return movieList
    }
}