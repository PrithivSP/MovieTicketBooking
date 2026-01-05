package controllers

import controllers.exceptions.ExitApp
import controllers.exceptions.ExitToUserMenu
import models.Booking
import models.Movie
import models.Screen
import models.Show
import models.dtos.NavResult
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
            try {
                currentFlowState = when (currentFlowState) {
                    AppFlowState.AUTH_MENU -> handleAuthMenu()
                    AppFlowState.LOGIN -> handleLogin()
                    AppFlowState.SIGNUP -> handleSignUp()

                    AppFlowState.USER_MENU -> handleUserMenu()

                    AppFlowState.BROWSE_MOVIES -> handleBrowseMovies()
                    AppFlowState.SEARCH_MOVIE -> handleSearchMovie()
                    AppFlowState.CHANGE_LOCATION -> handleChangeLocation()
                    AppFlowState.SHOW_HISTORY -> handleShowHistory()
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
            } catch (e: ExitApp) {
                currentFlowState = AppFlowState.AUTH_MENU
            } catch (e: ExitToUserMenu) {
                currentFlowState = AppFlowState.USER_MENU
            }

        } while (currentFlowState != AppFlowState.EXIT)

        println("End")
    }

    // state dispatchers
    private fun handleAuthMenu(): AppFlowState {
        return try {
            userController.showAuthenticationMenu()
        } catch (e: ExitToUserMenu) {
            AppFlowState.AUTH_MENU
        }
    }

    private fun handleLogin(): AppFlowState {
        return when(val result = userController.login()) {
            NavResult.Back -> AppFlowState.AUTH_MENU
            NavResult.Exit -> AppFlowState.EXIT
            NavResult.Retry -> AppFlowState.LOGIN

            is NavResult.Result -> {
                val user = result.result ?: return AppFlowState.LOGIN
                Session.login(user)
                AppFlowState.USER_MENU
            }
        }
    }

    private fun handleSignUp(): AppFlowState {
        return when (val result = userController.signUp()) {

            NavResult.Back ->
                AppFlowState.AUTH_MENU

            NavResult.Exit ->
                AppFlowState.EXIT

            NavResult.Retry ->
                AppFlowState.SIGNUP

            is NavResult.Result ->
                AppFlowState.AUTH_MENU
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

        val chosenMovie: Movie = movieController.chooseMovieFromTheater(selectedTheater) ?: return AppFlowState.SELECT_THEATER

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

        val selectedShow = showController.deriveShow(shows, selectedDate, selectedTime) ?: return AppFlowState.SELECT_TIME

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
        val totalPrice = Cache.totalPrice ?: return AppFlowState.SELECT_TIME
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

        if(booked != null) {
            bookingService.markSeatAsBooked(booked, selectedShow, selectedSeatSnapShots)
        }
        return AppFlowState.USER_MENU
    }

    private fun handleChangeLocation(): AppFlowState {
        val userId = Session.currentUser?.userId ?: return AppFlowState.AUTH_MENU

        userController.changeUserLocation(userId)

        return AppFlowState.USER_MENU
    }

    private fun handleShowHistory(): AppFlowState {
        val userId = Session.currentUser?.userId ?: return AppFlowState.AUTH_MENU

        bookingController.showHistory(userId)

        return AppFlowState.USER_MENU
    }

    private fun handleUpdateUserProfile(): AppFlowState {

        return AppFlowState.USER_MENU
    }



}
