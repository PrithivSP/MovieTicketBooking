package models

import models.enumerations.SeatTypes

class SeatSnapShot(
    val seatLabel: String,
    val seatType: SeatTypes,
    val seatPrice: Double
)