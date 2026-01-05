package models

import models.enumerations.BookingStatus
import java.time.LocalDateTime

class Booking(val bookingId: String,
              val movieId: String,
              val userId: String,
              val theaterId: String,
              val screenId: String,
              val showId: String,

              var bookingSeat: MutableSet<SeatSnapShot>,
              var cancelSeat: MutableSet<SeatSnapShot>,

              var bookingStatus: BookingStatus,
              var bookingPrice: Double,

              var bookingCreatedAt: LocalDateTime,
              var bookingCancelledAt: LocalDateTime? )