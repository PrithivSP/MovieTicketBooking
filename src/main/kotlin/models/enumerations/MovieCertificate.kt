package models.enumerations

enum class MovieCertificate(minAge: Byte) {
    U(3),
    UA(7),
    A(18),
    S(21)
}