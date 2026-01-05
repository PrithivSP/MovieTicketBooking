package services.interfaces

import models.Booking
import models.User

interface UserService {

    //creating user
    fun createUser(
        userName: String,
        userEmail: String,
        userPhoneNumber: String,
        userAge: Byte,
        userLocation: String,
        userPassword: String
    ): User?

    fun loginWithPhoneNumber(phoneNumber: String, password: String): User?
    fun loginWithEmailId(email: String, password: String): User?


    fun updateUserName(userId: String, newName: String)
    fun updateUserEmail(userId: String, newEmail: String)
    fun updateUserPhoneNumber(userId: String, newPhoneNumber: String)
    fun updateUserLocation(userId: String, newLocation: String)
    fun updateUserPassword(userId: String, newPassword: String)

    fun getHistory(userId: String): List<Booking>
    fun addBookingToUserHistory(userId: String, booking: Booking)

}