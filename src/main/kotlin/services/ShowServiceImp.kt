package services

import daos.ShowDAOImp
import daos.interfaces.ShowDAO
import models.Screen
import models.Show
import services.interfaces.ShowService
import kotlin.collections.contains

class ShowServiceImp(private val showDAO: ShowDAO = ShowDAOImp()): ShowService {
    override fun getShowsForMovieTheaterScreen(
        movieId: String,
        theaterId: String,
        screenId: String
    ): List<Show> {
        return showDAO.getShowByMovieTheaterScreen(movieId, theaterId, screenId)
    }

    override fun getInvalidSeats(
        requestedSeats: Set<String>,
        screen: Screen
    ): MutableSet<String> {
        val invalidSeats = mutableSetOf<String>()

        for (seat in requestedSeats) {
            if(!screen.seatTypeMap.contains(seat)) {
                invalidSeats.add(seat)
            }
        }

        return invalidSeats

    }

    override fun getAlreadyBookedSeats(
        requestedSeats: Set<String>,
        show: Show
    ): MutableSet<String> {
        val alreadyBookedSeats = mutableSetOf<String>()

        for (seat in requestedSeats) {
            val mapped = show.seats[seat]
            if(mapped != null) {
                alreadyBookedSeats.add(seat)
            }
        }

        return alreadyBookedSeats
    }

}