package views

import models.Movie
import utils.readInt
import utils.readString

class MovieView {
    fun showMoviesAndGetChoice(movies: List<Movie>): Int {
        val index = 1
        for(movie in movies) {
            println("$index) ${movie.movieName} | ${movie.getPrintableGenres()} | ${movie.movieDuration} | ${movie.movieLanguage} | ${movie.movieType.type}");
        }
        return readInt("Enter your movie choice (or 0 to go back): ")
    }

    fun showMovieDetails(movie: Movie) {
        println("Movie name       : ${movie.movieName}" )
        println("Certificate      : ${movie.movieEligibility}")
        println("Description      : ${movie.movieDescription}")
        println("Genre            : ${movie.getPrintableGenres()}")
        println("Language         : ${movie.movieLanguage}")
        println("Duration         : ${movie.movieDuration}")
        println("Movie Type       : ${movie.movieType}")

        if(movie is Movie.ThreeDMovie) {
            println("Deposit       : ₹${movie.glassDeposit}")
            println("Converted From 2D: ${if (movie.isConvertedFrom2D) "Yes" else "No"}")
            println("Motion Sickness: ${if(movie.motionSicknessRisk) "Yes" else "No"}")
        }
    }

    fun getMovieNameForSearch(): String {
        return readString("Enter movie name to search (or 0 to go back): ")
    }

    fun showNoMoviesFound() {
        println("\nNo movies available right now. Please try later")
    }

    fun getMovieConfirmationSelection(movieName: String): String {
        return readString("Confirm your selection \"${movieName}\" (y/n): ")
    }
}