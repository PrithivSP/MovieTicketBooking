package daos

import daos.DataStore
import daos.interfaces.BookingDAO
import models.Booking

class BookingDAOImp: BookingDAO {
    override fun addBooking(booking: Booking): Booking {
        DataStore.bookings[booking.bookingId] = booking
        return booking
    }
    override fun getBookingById(bookingId: String): Booking? {
        return DataStore.bookings[bookingId]
    }

    override fun updateBooking(booking: Booking) {
        DataStore.bookings[booking.bookingId] = booking
    }

    override fun getAllBookings(): List<Booking> {
        return DataStore.bookings.values.toList()
    }
}