package models.dtos

import models.Booking
import models.Movie
import models.Screen
import models.Show
import models.Theater

data class BookingDisplay(val booking: Booking,
    val theater: Theater,
    val screen: Screen,
    val movie: Movie,
    val show: Show)
