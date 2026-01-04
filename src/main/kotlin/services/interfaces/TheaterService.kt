package services.interfaces

import models.Screen
import models.Theater

interface TheaterService {

    fun getTheatersByMovieIdAndUserLocation(movieId: String, userLocation: String): List<Theater>

    fun getScreensForMovieAndTheater(movieId: String, theaterId: String): List<Screen>

    fun getTheaterById(theaterId: String): Theater?

    fun getScreenById(theaterId: String, screenId: String): Screen?

    fun getTheatersNearBy(userLocation: String): List<Theater>

}