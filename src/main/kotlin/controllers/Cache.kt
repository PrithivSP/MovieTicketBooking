package controllers

import models.Movie
import models.Screen
import models.SeatSnapShot
import models.Show
import models.Theater
import java.time.LocalDate
import java.time.LocalTime

internal object Cache {
    var selectedMovie: Movie? = null
    var selectedTheater: Theater? = null
    var selectedScreen: Screen? = null

    var selectedDate: LocalDate? = null
    var selectedTime: LocalTime? = null

    var selectedShow: Show? = null

    var selectedSeatSnapShots: MutableList<SeatSnapShot> = mutableListOf()
    var totalPrice: Double? = null

    fun clearFromMovie() {
        selectedTheater = null
        selectedScreen = null
    }

    fun clearFromTheater() {
        selectedScreen = null
    }

    fun clearCache() {
        selectedMovie = null
        selectedTheater = null
        selectedScreen = null
    }
}