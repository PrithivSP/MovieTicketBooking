package controllers

import models.Movie
import models.Screen
import models.SeatSnapShot
import models.Show
import models.Theater
import java.time.LocalDate
import java.time.LocalTime
import kotlin.properties.Delegates

internal object Cache {
    var selectedMovie: Movie? = null
    var selectedTheater: Theater? = null
    var selectedScreen: Screen? = null

    var selectedDate: LocalDate? = null
    var selectedTime: LocalTime? = null

    var selectedShow: Show? = null

    var selectedSeatSnapShots: MutableList<SeatSnapShot> = mutableListOf()

    var totalPrice: Double by Delegates.observable(0.0) { _ , old, new ->
        println("Price changed from $old to $new")
    }

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