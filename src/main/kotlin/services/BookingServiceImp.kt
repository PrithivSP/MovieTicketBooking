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
import services.interfaces.ShowService
import services.interfaces.TheaterService
import java.time.LocalDateTime
import java.util.UUID
import kotlin.concurrent.thread

class BookingServiceImp(private val bookingDAO: BookingDAO = BookingDAOImp(),
                        private val userDAO: UserDAO = UserDAOImp(),
                        private val theaterDAO: TheaterDAO = TheaterDAOImp(),
                        private val theaterService: TheaterService = TheaterServiceImp(),
                        private val showService: ShowService = ShowServiceImp(),
                        private val movieService: MovieService = MovieServiceImp()

): BookingService {
    override fun creatingBooking(userId: String,
                                 movieId: String,
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
            movieId,
            userId,
            theaterId,
            screenId,
            showId,
            seatSnapShots.toMutableSet(),
            cancelSeat.toMutableSet(),
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

    override fun getBookingDisplaySummary(booking: Booking): BookingDisplay {
        val booked = bookingDAO.getBookingById(booking.bookingId) ?: booking
        val theater = theaterService.getTheaterById(booking.theaterId)
        val screen = theaterService.getScreenById(booking.theaterId, booking.screenId)
        val movie = movieService.getMovieById(booking.movieId)
        val show = showService.getShowById(booking.showId)

        return BookingDisplay(booked, theater, screen, movie, show)
    }

    override fun releaseSeatAndUpdateBooking(
        booking: Booking,
        requestedSeats: Set<String>
    ): Booking {
        val show = showService.getShowById(booking.showId)

        for(seat in requestedSeats) {
            val bookedBy = show?.seats?.get(seat)

            if(bookedBy == booking.bookingId) {
                show.seats[seat] = null
            }
        }

        val currentSeats = booking.bookingSeat
        var price: Double = booking.bookingPrice

        val (cancelSeats, remainingSeats) = currentSeats.partition { seat -> seat.seatLabel in requestedSeats }

        cancelSeats.forEach { seat ->
            price -= seat.seatPrice
        }
        booking.cancelSeat.addAll(cancelSeats)
        booking.bookingSeat.clear()
        booking.bookingSeat.addAll(remainingSeats)

        if(booking.bookingSeat.isEmpty()) {
            booking.bookingCreatedAt = LocalDateTime.now()
            booking.bookingStatus = BookingStatus.CANCELLED
        }
        return booking
    }

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