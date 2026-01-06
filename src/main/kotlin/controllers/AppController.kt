package controllers

import controllers.exceptions.ExitApp
import controllers.exceptions.ExitToUserMenu
import models.Booking
import models.Movie
import models.Screen
import models.Show
import models.enumerations.AppFlowState
import services.BookingServiceImp
import services.interfaces.BookingService
import views.ShowView

class AppController {

    private var currentFlowState: AppFlowState = AppFlowState.AUTH_MENU

    // --- dependencies ---
    //service
    private val bookingService: BookingService = BookingServiceImp()

    //view
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
            try {
                // state dispatchers
                currentFlowState = when (currentFlowState) {
                    AppFlowState.AUTH_MENU -> handleAuthMenu()
                    AppFlowState.LOGIN -> handleLogin()
                    AppFlowState.SIGNUP -> handleSignUp()
                    AppFlowState.USER_MENU -> handleUserMenu()
                    AppFlowState.BROWSE_MOVIES -> handleBrowseMovies()
                    AppFlowState.SEARCH_MOVIE -> handleSearchMovie()
                    AppFlowState.CHANGE_LOCATION -> handleChangeLocation()
                    AppFlowState.SHOW_HISTORY -> handleShowHistory()
                    AppFlowState.CANCEL_BOOKING -> handleCancelBooking()
                    AppFlowState.UPDATE_PROFILE -> handleUpdateUserProfile()
                    AppFlowState.SELECT_MOVIE -> handleSelectMovie()
                    AppFlowState.SELECT_THEATER_FOR_MOVIE -> handleSelectTheaterForMovie()
                    AppFlowState.SELECT_THEATER -> handleSelectTheater()
                    AppFlowState.SELECT_MOVIE_FOR_THEATER -> handleSelectMovieForTheater()
                    AppFlowState.SELECT_SCREEN -> handleSelectScreen()
                    AppFlowState.SELECT_DATE -> handleSelectDate()
                    AppFlowState.SELECT_TIME -> handleSelectTime()
                    AppFlowState.SELECT_SEATS -> handleSelectSeat()
                    AppFlowState.BOOKING -> handleBooking()
                    AppFlowState.EXIT -> AppFlowState.EXIT
                }
            } catch (_: ExitApp) {
                currentFlowState = AppFlowState.EXIT
            } catch (_: ExitToUserMenu) {
                currentFlowState = AppFlowState.USER_MENU
            }

        } while (currentFlowState != AppFlowState.EXIT)

        println("End")
    }

    // state handlers
    private fun handleAuthMenu(): AppFlowState {
        return try {
            userController.showAuthenticationMenu()
        } catch (_: ExitToUserMenu) {
            AppFlowState.AUTH_MENU
        }
    }

    private fun handleLogin(): AppFlowState {
        return userController.login()
    }

    private fun handleSignUp(): AppFlowState {
        return userController.signUp()
    }

    private fun handleUserMenu(): AppFlowState {

        return browsingController.userMenu()
    }

    private fun handleBrowseMovies(): AppFlowState = AppFlowState.SELECT_MOVIE

    private fun handleSearchMovie(): AppFlowState {
        val searchedMovie = movieController.searchMovie() ?: return AppFlowState.USER_MENU

        Cache.selectedMovie = searchedMovie

        return AppFlowState.SELECT_THEATER_FOR_MOVIE
    }

    private fun handleSelectMovie(): AppFlowState {
        val movie = movieController.chooseMovieFromAll()

        if (movie == null) {
            Cache.clearCache()
            return AppFlowState.USER_MENU
        }

        Cache.selectedMovie = movie

        return AppFlowState.SELECT_THEATER_FOR_MOVIE
    }

    private fun handleSelectMovieForTheater(): AppFlowState {
        val selectedTheater = Cache.selectedTheater ?: return AppFlowState.SELECT_THEATER

        val chosenMovie: Movie =
            movieController.chooseMovieFromTheater(selectedTheater) ?: return AppFlowState.SELECT_THEATER

        Cache.selectedMovie = chosenMovie

        return AppFlowState.SELECT_SCREEN

    }

    private fun handleSelectTheater(): AppFlowState {
        val userLocation = Session.currentUser?.userLocation ?: return AppFlowState.AUTH_MENU

        val chosenTheater = theaterController.chooseTheaterNearBy(userLocation) ?: return AppFlowState.USER_MENU

        Cache.selectedTheater = chosenTheater

        return AppFlowState.SELECT_MOVIE_FOR_THEATER
    }

    private fun handleSelectTheaterForMovie(): AppFlowState {
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

        val selectedTheater = Cache.selectedTheater ?: return AppFlowState.SELECT_THEATER_FOR_MOVIE

        val chosenScreen: Screen =
            theaterController.chooseScreenForMovieAndTheater(selectedMovie, selectedTheater) ?: run {
                return AppFlowState.SELECT_MOVIE
            }

        Cache.selectedScreen = chosenScreen

        return AppFlowState.SELECT_DATE
    }

    private fun handleSelectDate(): AppFlowState {

        val selectedMovie = Cache.selectedMovie ?: return AppFlowState.SELECT_MOVIE
        val selectedTheater = Cache.selectedTheater ?: return AppFlowState.SELECT_THEATER_FOR_MOVIE
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
        val selectedTheater = Cache.selectedTheater ?: return AppFlowState.SELECT_THEATER_FOR_MOVIE
        val selectedScreen = Cache.selectedScreen ?: return AppFlowState.SELECT_SCREEN
        val selectedDate = Cache.selectedDate ?: return AppFlowState.SELECT_DATE

        val shows: List<Show> = showController.getShows(selectedMovie, selectedTheater, selectedScreen)

        if (shows.isEmpty()) {
            showView.showNoShowsAvailable()
            return AppFlowState.SELECT_DATE
        }

        val selectedTime =
            showController.chooseTimeForShowAndDate(shows, selectedDate) ?: return AppFlowState.SELECT_DATE

        val selectedShow =
            showController.deriveShow(shows, selectedDate, selectedTime) ?: return AppFlowState.SELECT_TIME

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
        val selectedMovie = Cache.selectedMovie ?: return AppFlowState.SELECT_MOVIE
        val selectedTheater = Cache.selectedTheater ?: return AppFlowState.SELECT_THEATER_FOR_MOVIE
        val selectedScreen = Cache.selectedScreen ?: return AppFlowState.SELECT_SCREEN
        val selectedShow: Show = Cache.selectedShow ?: return AppFlowState.SELECT_TIME
        val selectedSeatSnapShots = if (Cache.selectedSeatSnapShots.isEmpty()) {
            return AppFlowState.SELECT_SEATS
        } else {
            Cache.selectedSeatSnapShots
        }
        val totalPrice = Cache.totalPrice

        val userId: String = Session.currentUser?.userId ?: return AppFlowState.AUTH_MENU


        val booked: Booking? = bookingController.proceedBooking(
            userId,
            selectedMovie.movieId,
            selectedTheater.theaterId,
            selectedScreen.screenId,
            selectedShow.showId,
            selectedSeatSnapShots,
            totalPrice
        )

        if (booked != null) {
            bookingService.markSeatAsBooked(booked, selectedShow, selectedSeatSnapShots)
        }
        return AppFlowState.USER_MENU
    }

    private fun handleChangeLocation(): AppFlowState {
        val userId = Session.currentUser?.userId ?: return AppFlowState.AUTH_MENU

        return userController.changeUserLocation(userId)
    }

    private fun handleShowHistory(): AppFlowState {
        val userId = Session.currentUser?.userId ?: return AppFlowState.AUTH_MENU

        bookingController.showHistory(userId)

        return AppFlowState.USER_MENU
    }

    private fun handleCancelBooking(): AppFlowState {
        val userId = Session.currentUser?.userId ?: return AppFlowState.AUTH_MENU
        bookingController.cancelBooking(userId)
        return AppFlowState.USER_MENU
    }

    private fun handleUpdateUserProfile(): AppFlowState {
        val user = Session.currentUser ?: return AppFlowState.AUTH_MENU
        return userController.updateUserProfile(user)
    }

}
