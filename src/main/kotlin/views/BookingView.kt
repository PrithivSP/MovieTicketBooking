package views

class BookingView {
    fun showBookingError(error: String) {
        println("Failed to create booking: $error")
    }

    fun showBookingSuccess() {
        println("Booking successful!")
    }
}