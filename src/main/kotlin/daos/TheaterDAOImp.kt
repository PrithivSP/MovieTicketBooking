package daos

import daos.interfaces.TheaterDAO
import models.Screen
import models.Theater

class TheaterDAOImp: TheaterDAO {
    override fun getTheaterById(theaterId: String): Theater? {
        return DataStore.theaters[theaterId]
    }

    override fun getAllTheaters(): List<Theater> {
        return DataStore.theaters.values.toList()
    }

    override fun getScreensForTheaterId(theaterId: String): Map<String, Screen>  {
        return DataStore.theaters[theaterId]?.screens ?: mapOf()
    }

}