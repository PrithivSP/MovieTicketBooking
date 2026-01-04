package controllers

import models.Movie
import models.Screen
import models.SeatSnapShot
import models.Show
import models.Theater
import models.enumerations.SeatTypes
import services.ShowServiceImp
import services.interfaces.ShowService
import views.ConsoleView
import views.ShowView
import java.time.LocalDate
import java.time.LocalTime

class ShowController(
    private val showService: ShowService = ShowServiceImp(),
    private val showView: ShowView = ShowView()
) {

    fun getShows(
        movie: Movie, theater: Theater, screen: Screen
    ): List<Show> {

        return showService.getShowsForMovieTheaterScreen(movie.movieId, theater.theaterId, screen.screenId)
    }

    fun chooseDateForShows(shows: List<Show>): LocalDate? {
        val dates = shows.map { show ->
            show.showDateAndTime.toLocalDate()
        }.distinct().sorted()

        if (dates.isEmpty()) {
            showView.showNoDatesAvailable()
            return null
        }

        val dateChoice = showView.showDatesAndGetChoice(dates)

        if (dateChoice == 0) return null

        if (dateChoice < 1 || dateChoice > dates.size) {
            ConsoleView.printInvalidOptions()
            return null
        }

        return dates[dateChoice - 1]
    }

    fun chooseTimeForShowAndDate(shows: List<Show>, date: LocalDate): LocalTime? {
        val times = shows.filter { show ->
            show.showDateAndTime.toLocalDate() == date
        }.map { it ->
            it.showDateAndTime.toLocalTime()
        }.distinct().sorted()

        if (times.isEmpty()) {
            showView.showNoTimesAvailable()
        }

        val timeChoice = showView.showTimesAndGetChoice(times)

        if (timeChoice == 0) return null

        if (timeChoice < 1 || timeChoice > times.size) {
            ConsoleView.printInvalidOptions()
            return null
        }

        return times[timeChoice - 1]
    }

    fun deriveShow(shows: List<Show>, date: LocalDate, time: LocalTime): Show? {
        return shows.firstOrNull { show ->
            show.showDateAndTime.toLocalDate().equals(date) && show.showDateAndTime.toLocalTime().equals(time)
        }
    }

    private fun chooseSeat(screen: Screen, show: Show): MutableSet<String> {
        do {
            showView.displaySeats(screen, show)

            val requestedSeats = showView.getSeatsInput()

            if(requestedSeats.isEmpty()) return mutableSetOf()

            val invalidSeat = showService.getInvalidSeats(requestedSeats, screen)

            if(invalidSeat.isNotEmpty()) {
                showView.showInvalidSeats(invalidSeat)
                continue
            }

            val alreadyBookedSeats = showService.getAlreadyBookedSeats(requestedSeats, show)


            if(alreadyBookedSeats.isNotEmpty()) {
                showView.showAlreadyBookedSeats(alreadyBookedSeats)
                continue
            }

            return requestedSeats

        } while (true)
    }

    fun handleSeatSelection(screen: Screen, show: Show): MutableList<SeatSnapShot> {
        val requestedSeats = chooseSeat(screen, show)

        if(requestedSeats.isEmpty()) return mutableListOf()

        val seatSnapShots: MutableList<SeatSnapShot> = createSeatSnapShot(requestedSeats, screen, show)

        val totalPrice: Double = getTotalPrice(requestedSeats, screen, show)

        val confirmSeatSelection = showView.confirmSeatSelection(seatSnapShots, totalPrice)

        if(!confirmSeatSelection.equals("y", true) && !confirmSeatSelection.equals("yes", true)) {
            return mutableListOf()
        }

        Cache.totalPrice = totalPrice

        return seatSnapShots
    }

    private fun getTotalPrice(
        requestedSeats: MutableSet<String>,
        screen: Screen,
        show: Show
    ): Double {
        var total: Double = 0.0

        for(label in requestedSeats) {
            val type: SeatTypes = screen.seatTypeMap.get(label) ?: continue
            total += when(type) {
                SeatTypes.PREMIUM -> show.priceOfPremiumSeat
                SeatTypes.CLASSIC -> show.priceOfClassicSeat
                SeatTypes.ECONOMY -> show.priceOfEconomySeat
            }
        }

        return total
    }

    private fun createSeatSnapShot(
        requestedSeats: MutableSet<String>,
        screen: Screen,
        show: Show
    ): MutableList<SeatSnapShot> {
        val seatSnapShots: MutableList<SeatSnapShot> = mutableListOf()

        for (label in requestedSeats) {
            val type: SeatTypes = screen.seatTypeMap.get(label)  ?: continue

            val price: Double = when(type) {
                SeatTypes.PREMIUM -> show.priceOfPremiumSeat
                SeatTypes.CLASSIC -> show.priceOfClassicSeat
                SeatTypes.ECONOMY -> show.priceOfEconomySeat
            }
            seatSnapShots.add(SeatSnapShot(label, type, price))
        }

        return seatSnapShots
    }
}