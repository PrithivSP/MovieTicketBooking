package controllers

import controllers.exceptions.AuthenticationException
import models.Booking
import models.SeatSnapShot
import models.dtos.BookingDisplay
import models.enumerations.BookingStatus
import services.BookingServiceImp
import services.UserServiceImp
import services.interfaces.BookingService
import services.interfaces.UserService
import views.BookingView
import views.ConsoleView
import views.UserView
import java.lang.Exception
import java.time.LocalDateTime

class BookingController(
    private val bookingService: BookingService = BookingServiceImp(),
    private val userService: UserService = UserServiceImp(),

    private val userView: UserView = UserView(),
    private val bookingView: BookingView = BookingView()
) {

    fun proceedBooking(
        userId: String,
        movieId: String,
        theaterId: String,
        screenId: String,
        showId: String,
        seatSnapShots: List<SeatSnapShot>,
        price: Double
        ): Booking? {
        try {
            val booking = bookingService.creatingBooking(userId, movieId, theaterId, screenId, showId, seatSnapShots, listOf(), BookingStatus.BOOKED, price,
                LocalDateTime.now(), null)

            userService.addBookingToUserHistory(userId, booking)
            bookingView.showBookingSuccess()
            return booking
        } catch (e: Exception) {
            bookingView.showBookingError(e.message.toString())
        }

        return null
     }

    fun showHistory(userId: String) {
        ConsoleView.printHeader("User History")

        val userHistory = userService.getHistory(userId)

        if(userHistory.isEmpty()) {
            userView.showNoBookingsFound()
            return
        }

        for(booking in userHistory) {
            val bookingDisplay: BookingDisplay =  bookingService.getBookingDisplaySummary(booking)
            bookingView.showHistoryItem(bookingDisplay)
        }
    }
}