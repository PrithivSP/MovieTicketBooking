package services

import daos.UserDAOImp
import daos.interfaces.UserDAO
import models.User
import services.interfaces.UserService
import utils.isValidAge
import utils.isValidEmailId
import utils.isValidPassword
import utils.isValidPhoneNumber
import java.util.UUID

class UserServiceImp: UserService {
    private val userDAO: UserDAO = UserDAOImp()

    override fun createUser(
        userName: String,
        userEmail: String,
        userPhoneNumber: String,
        userAge: Byte,
        userLocation: String,
        userPassword: String
    ): User? {

        if(!userEmail.isValidEmailId()) {
            throw IllegalArgumentException("Invalid phone number format")
        }

        if(userDAO.getUserByEmailId(userEmail) != null) {
            throw IllegalArgumentException("Email is already registered.")
        }

        if(!userPhoneNumber.isValidPhoneNumber()) {
            throw IllegalArgumentException("Invalid Phone number format")
        }

        if(userDAO.getUserByPhoneNumber(userPhoneNumber) == null) {
            throw IllegalArgumentException("Phone number is already registered")
        }

        if(!userPassword.isValidPassword()) {
            throw IllegalArgumentException("Password does not meet the required rules")
        }

        if(!userAge.isValidAge()) {
            throw IllegalArgumentException("Age should be greater than 3 and less than 120")
        }

        val userId = UUID.randomUUID().toString()
        return User(userId, userName, userEmail, userPhoneNumber, userAge,
            userLocation,
            userPassword, mutableListOf())

    }

    override fun loginWithPhoneNumber(phoneNumber: String, password: String): User {
        if(!phoneNumber.isValidPhoneNumber()) {
            throw IllegalArgumentException("Invalid Phone number format")
        }

        val user = userDAO.getUserByPhoneNumber(phoneNumber)
            ?: throw IllegalArgumentException("No account found for this phone number")

        if(!user.verifyPassword(password)) {
            throw IllegalArgumentException("Incorrect password.")
        }

        return user
    }

    override fun loginWithEmailId(email: String, password: String): User {
        if(!email.isValidEmailId()) {
                throw IllegalArgumentException("Invalid Email format")
        }

        val user = userDAO.getUserByEmailId(email)
            ?: throw IllegalArgumentException("No account found for this email id")

        if(!user.verifyPassword(password)) {
            throw IllegalArgumentException("Incorrect password.")
        }

        return user
    }

    override fun updateUserName(userId: String, newName: String) {
        TODO("Not yet implemented")
    }

    override fun updateUserEmail(userId: String, newEmail: String) {
        TODO("Not yet implemented")
    }

    override fun updateUserPhoneNumber(userId: String, newPhoneNumber: String) {
        TODO("Not yet implemented")
    }

    override fun updateUserLocation(userId: String, newEmail: String) {
        TODO("Not yet implemented")
    }

    override fun updateUserPassword(userId: String, newPassword: String) {
        TODO("Not yet implemented")
    }
}