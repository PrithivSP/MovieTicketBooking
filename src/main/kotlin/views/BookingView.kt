package views

import models.*
import models.dtos.BookingDisplay
import java.time.format.DateTimeFormatter

class BookingView {
    fun showBookingError(error: String) {
        println("Failed to create booking: $error")
    }

    fun showBookingSuccess() {
        println("Booking successful!")
    }

    fun showHistoryFromList(userHistory: List<Booking>) {

    }

    fun showHistoryItem(bookingDisplay: BookingDisplay) {
        val booking: Booking = bookingDisplay.booking
        val theater: Theater? = bookingDisplay.theater
        val screen: Screen? = bookingDisplay.screen
        val movie: Movie? = bookingDisplay.movie
        val show: Show? = bookingDisplay.show

        println("Movie             : ${movie?.movieName ?: "N/A"}")
        println("Theater           : ${theater?.theaterName ?: "N/A"}")
        println("Screen            : ${screen?.screenName ?: "N/A"}")
        println("Show Date         : ${show?.showDateAndTime?.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}")
        println("Show Time         : ${show?.showDateAndTime?.format(DateTimeFormatter.ofPattern("hh : mm a"))}")

        booking.bookingSeat.takeIf { it.isNotEmpty() }?.joinToString(" ") { it.seatLabel}?.let {
            println("Booked Seats     : $it")
        }

        booking.cancelSeat
            .takeIf { it.isNotEmpty() }
            ?.joinToString(" ") { it.seatLabel }
            ?.let { println("Cancelled seats : $it") }

        println("Booking Status    : ${booking.bookingStatus}")

        if (booking.bookingPrice != 0.0) {
            println("Total Price       : ₹%.2f".format(booking.bookingPrice))
        }

        println("Booked At         : ${ booking.bookingCreatedAt?.format(DateTimeFormatter.ofPattern("dd MMM yyyy | hh : mm a")) ?: "N/A"}"
        )

        booking.bookingCancelledAt?.let {
            println(
                "Cancelled At      : ${
                    it.format(DateTimeFormatter.ofPattern("dd MMM yyyy | hh : mm a"))
                }"
            )
        }

        println("-----------------------------")
    }


}