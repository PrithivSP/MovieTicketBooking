package controllers

import controllers.exceptions.AuthenticationException
import models.Booking
import models.SeatSnapShot
import models.enumerations.BookingStatus
import services.BookingServiceImp
import services.UserServiceImp
import services.interfaces.BookingService
import services.interfaces.UserService
import views.BookingView
import java.lang.Exception
import java.time.LocalDateTime

class BookingController(
    private val bookingService: BookingService = BookingServiceImp(),
    private val userService: UserService = UserServiceImp(),

    private val bookingView: BookingView = BookingView()
) {

    fun proceedBooking(
        userId: String,
        theaterId: String,
        screenId: String,
        showId: String,
        seatSnapShots: List<SeatSnapShot>,
        price: Double
        ): Booking? {
        try {
            val booking = bookingService.creatingBooking(userId, theaterId, screenId, showId, seatSnapShots, listOf(), BookingStatus.BOOKED, price,
                LocalDateTime.now(), null)

            userService.addBookingToUserHistory(userId, booking)
            bookingView.showBookingSuccess()
            return booking
        } catch (e: Exception) {
            bookingView.showBookingError(e.message.toString())
        }

        return null
     }
}