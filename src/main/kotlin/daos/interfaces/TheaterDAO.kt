package daos.interfaces

import models.Screen
import models.Theater

interface TheaterDAO {
    fun getTheaterById(theaterId: String): Theater?
    fun getAllTheaters(): List<Theater>

    fun getScreensForTheaterId(theaterId: String): Map<String, Screen>
}