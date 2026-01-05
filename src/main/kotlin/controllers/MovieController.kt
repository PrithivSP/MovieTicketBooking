package controllers

import models.Movie
import models.Theater
import services.MovieServiceImp
import services.interfaces.MovieService
import views.ConsoleView
import views.MovieView

class MovieController(
    private val movieService: MovieService = MovieServiceImp(),
    private val movieView: MovieView = MovieView()
) {

    private fun chooseMovieFromList(movies: List<Movie>): Movie? {
        if (movies.isEmpty()) {
            return null
        }
        do {
            ConsoleView.printHeader("Browse Movies")
            val movieChoice = movieView.showMoviesAndGetChoice(movies)

            if (movieChoice == 0) return null

            if (movieChoice < 1 || movieChoice > movies.size) {
                ConsoleView.printInvalidOptions()
                continue
            }

            val selectedMovie = movies.get(movieChoice - 1)
            movieView.showMovieDetails(selectedMovie)
            return selectedMovie

        } while (true)
    }

    fun chooseMovieFromAll(): Movie? {
        val movies = movieService.getAllMovies()
        if (movies.isEmpty()) {
            movieView.showNoMoviesFound()
            return null
        }
        return chooseMovieFromList(movies)
    }

    fun searchMovie(): Movie? {
        do {

            val movieName = movieView.getMovieNameForSearch()

            if(movieName == "0") return null

            val matchedMovies = movieService.searchMovieByName(movieName)

            if(matchedMovies.isEmpty()) {
                movieView.showNoMoviesFound()
                continue
            }
            val movieChoice: Int = movieView.showMoviesAndGetChoice(matchedMovies)

            if(movieChoice == 0) {
                continue
            }

            if (movieChoice < 1 || movieChoice > matchedMovies.size) {
                ConsoleView.printInvalidOptions();
                continue;
            }

            val selectedMovie: Movie = matchedMovies[movieChoice - 1]
            val proceed = movieView.getMovieConfirmationSelection(selectedMovie.movieName)

            if(proceed.equals("y", true )|| proceed.equals("yes", true)) {
                return selectedMovie
            }
        } while (true)
    }

    fun chooseMovieFromTheater(theater: Theater): Movie? {
        val movieList = movieService.getMoviesForTheater(theater.movies)
        return chooseMovieFromList(movieList)
    }

}