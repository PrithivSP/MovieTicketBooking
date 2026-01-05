package services

import controllers.exceptions.AuthenticationException
import controllers.exceptions.ValidationException
import daos.BookingDAOImp
import daos.UserDAOImp
import daos.interfaces.BookingDAO
import daos.interfaces.UserDAO
import models.Booking
import models.User
import services.interfaces.UserService
import utils.isValidAge
import utils.isValidEmailId
import utils.isValidPassword
import utils.isValidPhoneNumber
import java.util.UUID

class UserServiceImp(private val bookingDAO: BookingDAO = BookingDAOImp(), private val userDAO: UserDAO = UserDAOImp()): UserService {

    override fun createUser(
        userName: String,
        userEmail: String,
        userPhoneNumber: String,
        userAge: Byte,
        userLocation: String,
        userPassword: String
    ): User {

        //validations and checking if another user exits with given details
        if(!userEmail.isValidEmailId()) {
            throw ValidationException("Invalid Email Id format")
        }

        if(!userDAO.isEmailAvailable(userEmail)) {
            throw ValidationException("Email Id is already registered.")
        }

        if(!userPhoneNumber.isValidPhoneNumber()) {
            throw ValidationException("Invalid Phone number format")
        }

        if(!userDAO.isPhoneNumberAvailable(userPhoneNumber)) {
            throw ValidationException("Phone number is already registered")
        }

        if(!userPassword.isValidPassword()) {
            throw ValidationException("Password does not meet the required rules")
        }

        if(!userAge.isValidAge()) {
            throw ValidationException("Age should be between 3 and 120")
        }


        //user creation
        val userId = UUID.randomUUID().toString()

        val user = User(userId, userName, userEmail, userPhoneNumber, userAge,
            userLocation,
            userPassword, mutableListOf())


        //adding user to DB
        userDAO.addUser(user)

        return user

    }

    override fun loginWithPhoneNumber(phoneNumber: String, password: String): User {
        if(!phoneNumber.isValidPhoneNumber()) {
            throw ValidationException("Invalid Phone number format")
        }

        val user = userDAO.getUserByPhoneNumber(phoneNumber)
            ?: throw AuthenticationException("No account found for this phone number")

        if(!user.verifyPassword(password)) {
            throw AuthenticationException("Incorrect password.")
        }

        return user
    }

    override fun loginWithEmailId(email: String, password: String): User {
        if(!email.isValidEmailId()) {
                throw ValidationException("Invalid Email Id format")
        }

        val user = userDAO.getUserByEmailId(email)
            ?: throw AuthenticationException("No account found for this email id")

        if(!user.verifyPassword(password)) {
            throw AuthenticationException("Incorrect password.")
        }

        return user
    }

    override fun updateUserName(userId: String, newName: String) {
        userDAO.getUserById(userId)?.userName = newName
    }

    override fun updateUserEmail(userId: String, newEmail: String) {
        userDAO.getUserById(userId)?.userEmail = newEmail
    }

    override fun updateUserPhoneNumber(userId: String, newPhoneNumber: String) {
        userDAO.getUserById(userId)?.userPhoneNumber = newPhoneNumber
    }

    override fun updateUserLocation(userId: String, newLocation: String) {
        userDAO.getUserById(userId)?.userLocation = newLocation
    }

    override fun updateUserPassword(userId: String, newPassword: String) {
       userDAO.getUserById(userId)?.changePassword(newPassword)
    }

    override fun getHistory(userId: String): List<Booking> {
        val user = userDAO.getUserById(userId) ?: return emptyList()

        return user.getBookingHistory()
            .mapNotNull { bookingId ->
                bookingDAO.getBookingById(bookingId)
            }
    }

    override fun addBookingToUserHistory(userId: String, booking: Booking) {
        val user = userDAO.getUserById(userId)
            ?: throw IllegalArgumentException("User not found: $userId")

        user.addBookingHistory(booking.bookingId)
    }
}