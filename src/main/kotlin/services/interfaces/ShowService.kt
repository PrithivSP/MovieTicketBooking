package services.interfaces

import models.Screen
import models.Show

interface ShowService {

    fun getShowById(showId: String): Show?
    fun getShowsForMovieTheaterScreen(movieId: String,
                                     theaterId: String,
                                     screenId: String): List<Show>

    fun getInvalidSeats(requestedSeats: Set<String>, screen: Screen): MutableSet<String>
    fun getAlreadyBookedSeats(requestedSeats: Set<String>, show: Show): MutableSet<String>
}