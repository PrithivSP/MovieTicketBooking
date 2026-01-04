package models

import java.time.LocalDateTime

class Show(
    val showId: String,
    val movieId: String,
    val theaterId: String,
    val screenId: String,
    val showDateAndTime: LocalDateTime,
    val priceOfPremiumSeat: Double,
    val priceOfClassicSeat: Double,
    val priceOfEconomySeat: Double,
    val seats: MutableMap<String, String?> // A1 -> bookingId
) {

}