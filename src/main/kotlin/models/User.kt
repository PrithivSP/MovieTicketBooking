package models

class User(
    val userId: String,
    var userName: String,
    var userEmail: String,
    var userPhoneNumber: String,
    var userAge: Byte,
    var userLocation: String,
    private var userPassword: String,
    private var userBookingHistory: MutableList<String>
) {
    fun addBookingHistory(bookingId: String) {
        userBookingHistory.add(bookingId)
    }

    fun getBookingHistory(): List<String> {
        return userBookingHistory.toList()
    }

    fun changePassword(password: String) {
        this.userPassword = password
    }

    fun verifyPassword(password: String): Boolean {
        return this.userPassword == password
    }
}