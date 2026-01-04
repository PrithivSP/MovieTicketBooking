package services

import daos.ShowDAOImp
import daos.TheaterDAOImp
import daos.interfaces.ShowDAO
import daos.interfaces.TheaterDAO
import models.Screen
import models.Theater
import services.interfaces.TheaterService

class TheaterServiceImp(
    val showDAO: ShowDAO = ShowDAOImp(),
    val theaterDAO: TheaterDAO = TheaterDAOImp()
) : TheaterService {

    override fun getTheatersByMovieIdAndUserLocation(
        movieId: String,
        userLocation: String
    ): List<Theater> {
        val theaters = theaterDAO.getAllTheaters()
        return theaters.filter { theater ->
            theater.theaterCity.equals(userLocation, true) &&
                    theater.movies.contains(movieId)
        }
    }

    override fun getScreensForMovieAndTheater(
        movieId: String,
        theaterId: String
    ): List<Screen> {
        val screens = theaterDAO.getScreensForTheaterId(theaterId).values
        val shows = showDAO.getShowsByMovieAndTheater(movieId, theaterId)

        //return all screens which have at least one show for given movie
        return screens.filter { screen ->
            shows.any { show ->
                show.screenId.equals(screen.screenId, true)
            }
        }
    }

    override fun getTheaterById(theaterId: String): Theater? {
        return theaterDAO.getTheaterById(theaterId)
    }

    override fun getScreenById(theaterId: String, screenId: String): Screen? {
        return theaterDAO.getScreensForTheaterId(theaterId)[screenId]
    }

    override fun getTheatersNearBy(userLocation: String): List<Theater> {
        return theaterDAO.getAllTheaters().filter { theater ->
            theater.theaterCity.equals(userLocation, true)
        }
    }


}