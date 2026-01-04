package daos.interfaces

import models.Booking

interface BookingDAO {
    fun addBooking(booking: Booking): Booking
    fun getBookingById(bookingId: String): Booking?
    fun updateBooking(booking: Booking)
    fun getAllBookings(): List<Booking>
}