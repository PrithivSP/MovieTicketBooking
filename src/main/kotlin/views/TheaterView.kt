package views

import models.Screen
import models.Theater
import utils.readInt

class TheaterView {
    fun showNoTheaterForMovie(movieName: String) {
        println("\nNo theaters found for \"$movieName\"")
    }

    fun showTheatersAndGetChoice(theaters: List<Theater>): Int {

        for ((index, theater) in theaters.withIndex()) {
            println(
                "${index + 1}) ${theater.theaterName} | ${theater.theaterAddress} | ${theater.theaterCity}",
            )
        }
        return readInt("Choose theater number (or 0 to go back): ")

    }

//    fun showTheaterDetails(theater: Theater){
//        println(theater.toString())
//    }

    fun showNoScreenFoundForTheater(theaterName: String) {
        println("No screens found for \"$theaterName\"")
    }

    fun showScreensAndGetChoice(screens: List<Screen>): Int {
        for ((index, screen) in screens.withIndex()) {
            println("${index + 1}) ${screen.screenName}")
        }

        return readInt("Choose screen number (or 0 to go back): ")
    }
}