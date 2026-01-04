package controllers

import controllers.exceptions.ExitApp
import models.Booking
import models.Screen
import models.Show
import models.enumerations.AppFlowState
import services.BookingServiceImp
import services.UserServiceImp
import services.interfaces.BookingService
import services.interfaces.UserService
import views.ShowView
import views.UserView

class AppController {

    private var currentFlowState: AppFlowState = AppFlowState.AUTH_MENU

    // --- dependencies ---
    //service
    private val userService: UserService = UserServiceImp()
    private val bookingService: BookingService = BookingServiceImp()

    //view
    private val userView: UserView = UserView()
    private val showView: ShowView = ShowView()

    //controllers
    private val browsingController: BrowsingController = BrowsingController()
    private val userController: UserController = UserController()
    private val movieController: MovieController = MovieController()
    private val theaterController: TheaterController = TheaterController()
    private val showController: ShowController = ShowController()
    private val bookingController: BookingController = BookingController()


    fun start() {

        do {

            currentFlowState = when (currentFlowState) {
                AppFlowState.AUTH_MENU -> handleAuthMenu()
                AppFlowState.LOGIN -> handleLogin()
                AppFlowState.SIGNUP -> handleSignUp()

                AppFlowState.USER_MENU -> handleUserMenu()

                AppFlowState.BROWSE_MOVIES -> handleBrowseMovies()
                AppFlowState.SEARCH_MOVIE -> handleSearchMovie()

                AppFlowState.SELECT_MOVIE -> handleSelectMovie()
                AppFlowState.SELECT_THEATER -> handleSelectTheater()
                AppFlowState.SELECT_SCREEN -> handleSelectScreen()
                AppFlowState.SELECT_DATE -> handleSelectDate()
                AppFlowState.SELECT_TIME -> handleSelectTime()
                AppFlowState.SELECT_SEATS -> handleSelectSeat()
                AppFlowState.BOOKING -> handleBooking()

                AppFlowState.EXIT -> AppFlowState.EXIT

            }

        } while (currentFlowState != AppFlowState.EXIT)

        println("End")
    }

    // state dispatchers
    private fun handleAuthMenu(): AppFlowState {
        return try {
            userController.showAuthenticationMenu()
        } catch (_: ExitApp) {
            AppFlowState.EXIT
        }

    }

    private fun handleLogin(): AppFlowState {
        return try {
            val user = userController.login()

            if (user != null) {
                println("Logging in...")
                Session.login(user)
                AppFlowState.USER_MENU

            } else {
                AppFlowState.AUTH_MENU
            }
        } catch (_: ExitApp) {
            AppFlowState.EXIT
        }
    }

    private fun handleSignUp(): AppFlowState {
        return try {
            userController.signUp()
            AppFlowState.AUTH_MENU
        } catch (_: ExitApp) {
            AppFlowState.EXIT
        }
    }

    private fun handleUserMenu(): AppFlowState {

        return browsingController.userMenu()
    }

    private fun handleBrowseMovies(): AppFlowState {
        return AppFlowState.SELECT_MOVIE
    }

    private fun handleSearchMovie(): AppFlowState {
        val searchedMovie = movieController.searchMovie() ?: return AppFlowState.USER_MENU

        Cache.selectedMovie = searchedMovie

        return AppFlowState.SELECT_THEATER
    }

    private fun handleSelectMovie(): AppFlowState {
        val movie = movieController.chooseMovieFromAll()

        if (movie == null) {
            Cache.clearCache()
            return AppFlowState.USER_MENU
        }

        Cache.selectedMovie = movie

        return AppFlowState.SELECT_THEATER
    }

    private fun handleSelectTheater(): AppFlowState {
        val userLocation = Session.currentUser?.userLocation ?: return AppFlowState.AUTH_MENU

        val selectedMovie = Cache.selectedMovie ?: return AppFlowState.SELECT_MOVIE

        val theater = theaterController.chooseTheaterForMovie(selectedMovie, userLocation) ?: run {
            Cache.clearFromTheater()
            return AppFlowState.USER_MENU
        }

        Cache.selectedTheater = theater

        return AppFlowState.SELECT_SCREEN
    }

    private fun handleSelectScreen(): AppFlowState {

        val selectedMovie = Cache.selectedMovie ?: return AppFlowState.SELECT_MOVIE

        val selectedTheater = Cache.selectedTheater ?: return AppFlowState.SELECT_THEATER

        val chosenScreen: Screen =
            theaterController.chooseScreenForMovieAndTheater(selectedMovie, selectedTheater) ?: run {
                return AppFlowState.SELECT_MOVIE
            }

        Cache.selectedScreen = chosenScreen

        return AppFlowState.SELECT_DATE
    }

    private fun handleSelectDate(): AppFlowState {

        val selectedMovie = Cache.selectedMovie ?: return AppFlowState.SELECT_MOVIE
        val selectedTheater = Cache.selectedTheater ?: return AppFlowState.SELECT_THEATER
        val chosenScreen: Screen = Cache.selectedScreen ?: return AppFlowState.SELECT_SCREEN

        val shows: List<Show> = showController.getShows(selectedMovie, selectedTheater, chosenScreen)

        if (shows.isEmpty()) {
            showView.showNoShowsAvailable()
            return AppFlowState.SELECT_SCREEN
        }

        val chosenDate = showController.chooseDateForShows(shows) ?: return AppFlowState.SELECT_SCREEN

        Cache.selectedDate = chosenDate

        //need to remove cache
        return AppFlowState.SELECT_TIME
    }

    private fun handleSelectTime(): AppFlowState {

        val selectedMovie = Cache.selectedMovie ?: return AppFlowState.SELECT_MOVIE
        val selectedTheater = Cache.selectedTheater ?: return AppFlowState.SELECT_THEATER
        val selectedScreen = Cache.selectedScreen ?: return AppFlowState.SELECT_SCREEN
        val selectedDate = Cache.selectedDate ?: return AppFlowState.SELECT_DATE

        val shows: List<Show> = showController.getShows(selectedMovie, selectedTheater, selectedScreen)

        if (shows.isEmpty()) {
            showView.showNoShowsAvailable()
            return AppFlowState.SELECT_DATE
        }

        val selectedTime =
            showController.chooseTimeForShowAndDate(shows, selectedDate) ?: return AppFlowState.SELECT_DATE

        val selectedShow = showController.deriveShow(shows, selectedDate, selectedTime)

        Cache.selectedTime = selectedTime
        Cache.selectedShow = selectedShow

        return AppFlowState.SELECT_SEATS

    }

    private fun handleSelectSeat(): AppFlowState {

        val selectedScreen: Screen = Cache.selectedScreen ?: return AppFlowState.SELECT_SCREEN
        val selectedShow: Show = Cache.selectedShow ?: return AppFlowState.SELECT_TIME

        val selectedSeatSnapShots = showController.handleSeatSelection(selectedScreen, selectedShow)

        if (selectedSeatSnapShots.isEmpty()) {
            return AppFlowState.SELECT_TIME
        }

        Cache.selectedSeatSnapShots = selectedSeatSnapShots


        // browsing booking
        return AppFlowState.BOOKING
    }

    private fun handleBooking(): AppFlowState {
        val selectedTheater = Cache.selectedTheater ?: return AppFlowState.SELECT_THEATER
        val selectedScreen = Cache.selectedScreen ?: return AppFlowState.SELECT_SCREEN
        val selectedShow: Show = Cache.selectedShow ?: return AppFlowState.SELECT_TIME
        val selectedSeatSnapShots = if (Cache.selectedSeatSnapShots.isEmpty()) {
            return AppFlowState.SELECT_SEATS
        } else {
            Cache.selectedSeatSnapShots
        }
        val totalPrice = Cache.totalPrice ?: return AppFlowState.SELECT_TIME
        val userId: String = Session.currentUser?.userId ?: return AppFlowState.AUTH_MENU
        val booked: Booking? = bookingController.proceedBooking(
            userId,
            selectedTheater.theaterId,
            selectedScreen.screenId,
            selectedShow.showId,
            selectedSeatSnapShots,
            totalPrice
        )

        if(booked != null) {
            bookingService.markSeatAsBooked(booked, selectedShow, selectedSeatSnapShots)
        }
        return AppFlowState.USER_MENU
    }


}
