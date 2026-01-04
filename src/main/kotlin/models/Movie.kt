package models

import models.enumerations.Genre
import models.enumerations.Languages
import models.enumerations.MovieCertificate
import models.enumerations.MovieType

sealed class Movie(
    val movieId: String,
    val movieName: String,
    val movieEligibility: MovieCertificate,
    val movieDescription: String,
    val movieGenres: List<Genre>,
    val movieLanguage: Languages,
    val movieDuration: String,
) {

    abstract val movieType: MovieType

    fun getPrintableGenres(): String {
        return movieGenres.joinToString(separator = ", ")
    }

    class RegularMovie(
        movieId: String,
        movieName: String,
        movieEligibility: MovieCertificate,
        movieDescription: String,
        movieGenres: List<Genre>,
        movieLanguage: Languages,
        movieDuration: String
    ) : Movie(
        movieId, movieName, movieEligibility, movieDescription, movieGenres, movieLanguage, movieDuration
    ) {
        override val movieType: MovieType = MovieType.TWO_D
    }

    class ThreeDMovie(
        movieId: String,
        movieName: String,
        movieEligibility: MovieCertificate,
        movieDescription: String,
        movieGenres: List<Genre>,
        movieLanguage: Languages,
        movieDuration: String,
        val glassDeposit: Double,
        val motionSicknessRisk: Boolean,
        val isConvertedFrom2D: Boolean
    ) : Movie(
        movieId,
        movieName,
        movieEligibility,
        movieDescription,
        movieGenres,
        movieLanguage,
        movieDuration,
    ) {
        override val movieType: MovieType = MovieType.THREE_D
    }
}



