package controllers

import models.Booking
import models.SeatSnapShot
import models.dtos.BookingDisplay
import models.dtos.NavResult
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

    fun cancelBooking(userId: String): NavResult<Unit> {

        ConsoleView.printHeader("Cancel Tickets")

        val cancellableBookings =  bookingService.getCancellableBookings(userId)
        if(cancellableBookings.isEmpty()) {
            bookingView.showNoCancellableBookings()
            return NavResult.Back
        }

        val bookingDisplays = mutableListOf<BookingDisplay>()

        for(booking in cancellableBookings) {
            val bookingDisplay = bookingService.getBookingDisplaySummary(booking)
            bookingDisplays.add(bookingDisplay)
        }

        bookingView.showCancellableBookings(bookingDisplays)

        var chosenBooking: Booking

        do {
            val cancelChoice: Int = when(val input = bookingView.getCancelBookingChoice()) {
                NavResult.Back -> return NavResult.Back
                NavResult.Exit -> return NavResult.Exit
                is NavResult.Result -> input.result

                NavResult.Retry -> continue
            }

            if(cancelChoice < 1 || cancelChoice > cancellableBookings.size) {
                ConsoleView.printInvalidOptions()
                continue
            } else {
                chosenBooking = cancellableBookings[cancelChoice - 1]
                break
            }


        } while (true)

        // validate
        var requestedSeats: Set<String>
        do {
            requestedSeats = when(val seatInput = bookingView.getSeatCancellationInput()) {
                NavResult.Back -> return NavResult.Back
                NavResult.Exit -> return NavResult.Exit
                is NavResult.Result ->  seatInput.result
                NavResult.Retry -> continue
            }

            val bookedSeats = chosenBooking.bookingSeat.map { it.seatLabel }

            val invalidSeats = requestedSeats.filter { seat ->
                !bookedSeats.contains(seat)
            }

            if(invalidSeats.isNotEmpty()) {
                bookingView.showSeatValidationError(invalidSeats.joinToString(" "))
                continue
            } else {
                break
            }
        } while (true)

        val updatedBooking = bookingService.releaseSeatAndUpdateBooking(chosenBooking, requestedSeats)


        if(updatedBooking.bookingSeat.isEmpty()) {
            bookingView.showAllTicketsCancelled()
        } else {
            bookingView.showPartialCancellationCompleted()
        }

        Cache.totalPrice = updatedBooking.bookingPrice

        return NavResult.Back
    }
}