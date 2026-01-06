package controllers

import controllers.exceptions.ExitToUserMenu
import models.Movie
import models.Theater
import models.enumerations.Genre
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

            val selectedMovie = movies[movieChoice - 1]
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

            if (movieName == "0") return null

            val matchedMovies = movieService.searchMovieByName(movieName)

            if (matchedMovies.isEmpty()) {
                movieView.showNoMoviesFound()
                continue
            }
            val movieChoice: Int = movieView.showMoviesAndGetChoice(matchedMovies)

            if (movieChoice == 0) {
                continue
            }

            if (movieChoice < 1 || movieChoice > matchedMovies.size) {
                ConsoleView.printInvalidOptions()
                continue
            }

            val selectedMovie: Movie = matchedMovies[movieChoice - 1]
            val proceed = movieView.getMovieConfirmationSelection(selectedMovie.movieName)

            if (proceed.equals("y", true) || proceed.equals("yes", true)) {
                return selectedMovie
            }
        } while (true)
    }

    fun chooseMovieFromTheater(theater: Theater): Movie? {
        val movieList = movieService.getMoviesForTheater(theater.movies)
        return chooseMovieFromList(movieList)
    }

    fun chooseMovieFromGenre(): Movie? {

        do {
            var chosenGenre: Genre
            ConsoleView.printHeader("Choose Genre")
            val genres = Genre.entries.toList()
            val genreChoice = movieView.showGenreAndGetChoice(genres)

            if(genreChoice == 0) return null
            if(genreChoice == -1) throw ExitToUserMenu()

            if(genreChoice < 1 || genreChoice > genres.size) {
                ConsoleView.printInvalidOptions()
                continue
            }

            chosenGenre = genres[genreChoice - 1]

            val movieWithSelectedGenre = movieService.getMoviesForGenre(chosenGenre)

            if(movieWithSelectedGenre.isEmpty()) {
                movieView.showNoMoviesFound()
                continue
            }
            val chosenMovie = chooseMovieFromList(movieWithSelectedGenre)
            if(chosenMovie == null) {
                continue
            } else {
                return chosenMovie
            }
        } while (true)

    }

}