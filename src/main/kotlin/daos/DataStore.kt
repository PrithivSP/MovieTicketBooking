package daos

import models.Booking
import models.Movie
import models.Screen
import models.Show
import models.Theater
import models.User
import models.enumerations.Genre
import models.enumerations.Languages
import models.enumerations.MovieCertificate
import models.enumerations.SeatTypes
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

internal object DataStore {
    val users: MutableMap<String, User> = mutableMapOf()
    val theaters: MutableMap<String, Theater> = mutableMapOf()
    val movies: MutableMap<String, Movie> = mutableMapOf()
    val shows: MutableMap<String, Show> = mutableMapOf()
    val bookings: MutableMap<String, Booking> = mutableMapOf()

    init {
        addSampleData()
    }

    private fun addSampleData() {

        // ================= USERS =================
        val u1 = User(
            UUID.randomUUID().toString(),
            "Max",
            "max@zoho.com",
            "9786337697",
            25,
            "chennai",
            "max12345",
            mutableListOf()
        )

        val u2 = User(
            UUID.randomUUID().toString(),
            "Holly",
            "holly@zoho.com",
            "0987654321",
            18,
            "chennai",
            "holly1234",
            mutableListOf()
        )

        users[u1.userId] = u1
        users[u2.userId] = u2

        // ================= MOVIES =================
        val m1 = Movie.ThreeDMovie(
            "M1",
            "Inception",
            MovieCertificate.S,
            "A thief enters dreams to steal secrets.",
            listOf(Genre.SCI_FI),
            Languages.ENGLISH,
            "2h 28m",
            glassDeposit = 50.0,
            motionSicknessRisk = true,
            isConvertedFrom2D = true
        )

        val m2 = Movie.RegularMovie(
            "M2",
            "Vikram",
            MovieCertificate.UA,
            "A special agent on a dangerous mission.",
            listOf(Genre.ACTION),
            Languages.TAMIL,
            "2h 55m"
        )

        movies[m1.movieId] = m1
        movies[m2.movieId] = m2

        //================= COMMON VALUES =================
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)

        val theaterMovies = mutableSetOf("M1")

        // ================= THEATER LOOP =================
        for (t in 1..3) {

            // ---------- SEAT TYPE MAPS ----------
            val seatTypeMap1 = mutableMapOf<String, SeatTypes>()
            val seatTypeMap2 = mutableMapOf<String, SeatTypes>()

            for (i in 1..5) {
                seatTypeMap1["A$i"] = SeatTypes.PREMIUM
                seatTypeMap1["B$i"] = SeatTypes.CLASSIC
                seatTypeMap1["C$i"] = SeatTypes.ECONOMY

                seatTypeMap2["A$i"] = SeatTypes.PREMIUM
                seatTypeMap2["B$i"] = SeatTypes.CLASSIC
                seatTypeMap2["C$i"] = SeatTypes.ECONOMY
            }

            // ---------- SCREENS ----------
            val screen1 = Screen("S1", "Screen 1", seatTypeMap1, mutableListOf<String>())
            val screen2 = Screen("S2", "Screen 2", seatTypeMap2, mutableListOf<String>())

            val screens = mutableMapOf(
                screen1.screenId to screen1,
                screen2.screenId to screen2
            )

            // ---------- THEATER ----------
            val theater = Theater(
                theaterId = "T$t",
                theaterName = when (t) {
                    1 -> "PVR Cinemas"
                    2 -> "Rohini Cinemas"
                    else -> "Thinnapa Cinemas"
                },
                theaterAddress = when (t) {
                    1 -> "Anna Nagar"
                    2 -> "Koyambedu"
                    else -> "Karur"
                },
                theaterCity = if (t <= 2) "chennai" else "karur",
                screens = screens,
                movies = theaterMovies
            )

            theaters[theater.theaterId] = theater

            // ---------- SHOWS ----------

            val showTimes = listOf(
                LocalTime.of(10, 0),
                LocalTime.of(18, 30)
            )

            val dates = listOf(today, tomorrow)

            var showCounter = 1

            for (screen in screens.values) {
                for (date in dates) {
                    for (time in showTimes) {

                        val showDateTime = LocalDateTime.of(date, time)

                        val show = Show(
                            showId = "SH${t}_${screen.screenId}_$showCounter",
                            movieId = "M1",
                            theaterId = theater.theaterId,
                            screenId = screen.screenId,
                            showDateAndTime = showDateTime,
                            priceOfPremiumSeat = 300.0,
                            priceOfClassicSeat = 250.0,
                            priceOfEconomySeat = 180.0,
                            seats = mutableMapOf()
                        )

                        shows[show.showId] = show
                        showCounter++
                    }
                }
            }
        }
    }

}