package models

import models.enumerations.BookingStatus
import java.time.LocalDateTime

class Booking(val bookingId: String,
              val userId: String,
              val theaterId: String,
              val screenId: String,
              val showId: String,

              var bookingSeat: List<SeatSnapShot>,
              var cancelSeat: List<SeatSnapShot>,

              var bookingStatus: BookingStatus,
              var bookingPrice: Double,

              val bookingCreatedAt: LocalDateTime,
              var bookingCancelledAt: LocalDateTime? ) {

}