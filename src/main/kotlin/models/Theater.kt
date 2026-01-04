package models

class Theater(
    val theaterId: String,
    val theaterName: String,
    val theaterAddress: String,
    val theaterCity: String,
    val screens: MutableMap<String, Screen>,
    val movies: MutableSet<String>
)
