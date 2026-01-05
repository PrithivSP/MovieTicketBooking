package services.interfaces


import daos.BookingDAOImp
import daos.interfaces.BookingDAO
import models.Booking
import models.SeatSnapShot
import models.Show
import models.dtos.BookingDisplay
import models.enumerations.BookingStatus
import java.time.LocalDateTime
import java.util.UUID

interface BookingService {
    fun creatingBooking(userId: String,
                        movieId: String,
                        theaterId: String,
                        screenId: String,
                        showId: String,
                        seatSnapShots: List<SeatSnapShot>,
                        cancelSeat: List<SeatSnapShot>,
                        bookingStatus: BookingStatus,
                        bookingPrice: Double,
                        bookingCreatedAt: LocalDateTime,
                        bookingCancelledAt: LocalDateTime?): Booking

    fun getUserBooking(userId: String): List<String>

    fun getCancellableBookings(userId: String) : List<Booking>

    fun getBookingDisplaySummary(booking: Booking): BookingDisplay

//    fun releaseSeatAndUpdateBooking(booking: Booking, requestedSeats: Set<String>): Booking

    fun markSeatAsBooked(booked: Booking, show: Show, seatSnapShots: List<SeatSnapShot>)
}