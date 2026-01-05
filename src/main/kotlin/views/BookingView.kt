package views

import models.*
import models.dtos.BookingDisplay
import models.dtos.NavResult
import utils.readInt
import utils.readString
import java.time.format.DateTimeFormatter

class BookingView {
    fun showBookingError(error: String) {
        println("Failed to create booking: $error")
    }

    fun showBookingSuccess() {
        println("Booking successful!")
    }

    fun showNoCancellableBookings() = println("No cancellable tickets")

    fun showHistoryItem(bookingDisplay: BookingDisplay) {
        val booking: Booking = bookingDisplay.booking
        val theater: Theater? = bookingDisplay.theater
        val screen: Screen? = bookingDisplay.screen
        val movie: Movie? = bookingDisplay.movie
        val show: Show? = bookingDisplay.show

        println("Movie          : ${movie?.movieName ?: "N/A"}")
        println("Theater           : ${theater?.theaterName ?: "N/A"}")
        println("Screen            : ${screen?.screenName ?: "N/A"}")
        println("Show Date         : ${show?.showDateAndTime?.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))}")
        println("Show Time         : ${show?.showDateAndTime?.format(DateTimeFormatter.ofPattern("hh : mm a"))}")

        booking.bookingSeat.takeIf { it.isNotEmpty() }?.joinToString(" ") { it.seatLabel}?.let {
            println("Booked Seats      : $it")
        }

        booking.cancelSeat
            .takeIf { it.isNotEmpty() }
            ?.joinToString(" ") { it.seatLabel }
            ?.let { println("Cancelled seats   : $it") }

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

    fun showCancellableBookings(bookingDisplays: List<BookingDisplay>) {
        for((index, bookingDisplay) in bookingDisplays.withIndex()) {
            print("${index+1}) ")
            showHistoryItem(bookingDisplay)
        }
    }

    fun showSeatValidationError(seats: String) = println("\nInvalid Seats: $seats")

    fun showAllTicketsCancelled() = println("\nAll seats cancelled. Booking marked as CANCELLED")

    fun showPartialCancellationCompleted() = println("\nPartial cancellation done")

    fun getCancelBookingChoice(): NavResult<Int> {
        val input = readInt("Select booking (or 0 to go back): ")

        return when(input) {
            0 -> NavResult.Exit
            -1 -> NavResult.Exit
            1, 2 -> NavResult.Result(input)
            else -> {
                ConsoleView.printInvalidOptions()
                NavResult.Retry
            }
        }
    }

    fun getSeatCancellationInput(): NavResult<Set<String>> {
        val requestedSeats = readString("Enter seats to cancel (e.g. A1 A2) or 0 to go back: ").trim().uppercase()
        if(requestedSeats == "0") {
            return NavResult.Back
        }

        if(requestedSeats == "-1") {
            return NavResult.Exit
        }
        val parsedSeats = requestedSeats.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toMutableSet()
        return NavResult.Result(parsedSeats)
    }

}