package models

import models.enumerations.SeatTypes

class Seat(
    val seatLabel: String,
    val seatTypes: SeatTypes,
    var bookingId: String? = null
)