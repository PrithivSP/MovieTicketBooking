package models

import models.enumerations.SeatTypes

class Screen(
    val screenId: String,
    val screenName: String,
    val seatTypeMap: MutableMap<String, SeatTypes>,
    val shows: MutableList<String>
)