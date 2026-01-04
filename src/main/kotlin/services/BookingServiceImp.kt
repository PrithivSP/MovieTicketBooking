package services

import daos.BookingDAOImp
import daos.TheaterDAOImp
import daos.UserDAOImp
import daos.interfaces.BookingDAO
import daos.interfaces.TheaterDAO
import daos.interfaces.UserDAO
import models.Booking
import models.SeatSnapShot
import models.Show
import models.User
import models.dtos.BookingDisplay
import models.enumerations.BookingStatus
import services.interfaces.BookingService
import services.interfaces.MovieService
import services.interfaces.TheaterService
import java.time.LocalDateTime
import java.util.UUID

class BookingServiceImp(private val bookingDAO: BookingDAO = BookingDAOImp(),
    private val userDAO: UserDAO = UserDAOImp(),
    private val theaterDAO: TheaterDAO = TheaterDAOImp(),
            private val theaterService: TheaterService = TheaterServiceImp(),
            private val movieService: MovieService = MovieServiceImp()

): BookingService {
    override fun creatingBooking(userId: String,
                        theaterId: String,
                        screenId: String,
                        showId: String,
                        seatSnapShots: List<SeatSnapShot>,
                        cancelSeat: List<SeatSnapShot>,
                        bookingStatus: BookingStatus,
                        bookingPrice: Double,
                        bookingCreatedAt: LocalDateTime,
                        bookingCancelledAt: LocalDateTime?): Booking {
        val bookingId = UUID.randomUUID().toString()

        val book: Booking = Booking(bookingId,
            userId,
            theaterId,
            screenId,
            showId,
            seatSnapShots,
            cancelSeat,
            bookingStatus,
            bookingPrice,
            bookingCreatedAt,
            bookingCancelledAt)

        return bookingDAO.addBooking(book)
    }

    override fun getUserBooking(userId: String): List<String> {
        val user: User = userDAO.getUserById(userId) ?: return listOf()
        if(user.getBookingHistory().isEmpty()) {
            return listOf()
        }
        return user.getBookingHistory()
    }

    override fun getCancellableBookings(userId: String): List<Booking> {
        val user = userDAO.getUserById(userId) ?: return emptyList()

        return user.getBookingHistory()
            .mapNotNull { bookingId ->
                bookingDAO.getBookingById(bookingId)
            }
            .filter { booking ->
                booking.bookingStatus == BookingStatus.BOOKED
            }
    }

//    override fun getBookingDisplaySummary(booking: Booking): BookingDisplay {
//        TODO("Not yet implemented")
//    }

//    override fun releaseSeatAndUpdateBooking(
//        booking: Booking,
//        requestedSeats: Set<String>
//    ): Booking {
//        TODO("Not yet implemented")
//    }

    override fun markSeatAsBooked(
        booked: Booking,
        show: Show,
        seatSnapShots: List<SeatSnapShot>
    ) {
        for (seat in seatSnapShots) {
            val label = seat.seatLabel.uppercase()
            show.seats[label] = booked.bookingId
        }
    }

}