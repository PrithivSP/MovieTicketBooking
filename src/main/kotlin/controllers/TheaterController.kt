package controllers

import controllers.exceptions.ExitToUserMenu
import models.Movie
import models.Screen
import models.Theater
import services.TheaterServiceImp
import services.interfaces.TheaterService
import views.ConsoleView
import views.TheaterView

class TheaterController(
    val theaterService: TheaterService = TheaterServiceImp(),
    val theaterView: TheaterView = TheaterView()
) {

    private fun chooseTheaterFromList(theaters: List<Theater>): Theater? {

        do {
            ConsoleView.printHeader("Choose Theater")
            val theaterChoice: Int = theaterView.showTheatersAndGetChoice(theaters)

            // navigation checks
            if (theaterChoice == 0) return null;
            if (theaterChoice == -1) throw ExitToUserMenu()

            // input validity checks
            if (theaterChoice < 1 || theaterChoice > theaters.size) {
                ConsoleView.printInvalidOptions()
            } else {
                return theaters[theaterChoice - 1]
            }
        } while (true)

    }

    fun chooseTheaterForMovie(movie: Movie, userLocation: String): Theater? {

        val theaters: List<Theater> =
            theaterService.getTheatersByMovieIdAndUserLocation(movie.movieId, userLocation)

        return if (theaters.isEmpty()) {
            theaterView.showNoTheaterForMovie(movie.movieName)
            null
        } else {
            chooseTheaterFromList(theaters)
        }

    }

    fun chooseScreenForMovieAndTheater(chosenMovie: Movie, chosenTheater: Theater): Screen? {
        val screens: List<Screen> = theaterService.getScreensForMovieAndTheater(chosenMovie.movieId, chosenTheater.theaterId)

        if(screens.isEmpty()) {
            theaterView.showNoScreenFoundForTheater(chosenTheater.theaterName)
            return null
        }

        do {
            ConsoleView.printHeader("Choose Screen")
            val screenChoice: Int = theaterView.showScreensAndGetChoice(screens)

            // navigation checks
            if(screenChoice == 0) return null
            if(screenChoice == -1) throw ExitToUserMenu()

            //input validity checks
            if (screenChoice < 1 || screenChoice > screens.size) {
                ConsoleView.printInvalidOptions()
            } else {
                return screens[screenChoice - 1]
            }
        } while (true)
    }



    fun proceedWithScreenAndShowSelection(chosenMovie: Movie, chosenTheater: Theater) {
        val chosenScreen = chooseScreenForMovieAndTheater(chosenMovie, chosenTheater) ?: return


    }
}