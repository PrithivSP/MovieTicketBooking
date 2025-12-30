package controllers

import controllers.exceptions.ExitApp
import models.User
import services.interfaces.UserService
import views.ConsoleView
import views.UserView

class UserController(private val userService: UserService, private val userView: UserView) {

    // authentication
    fun authentication(): User? {
        do {
            val authChoice: Int = userView.getAuthChoiceView()

            when(authChoice) {
                1 -> return login()
                0, -1 -> throw ExitApp()
                else -> ConsoleView.printInvalidOptions()
            }
        } while (true)
    }

    // logging in
    private fun login(): User? {

        do {

            ConsoleView.printHeader("Login")
            val loginChoice = userView.getLoginChoice()

            when(loginChoice) {
                1 -> return loginWithPhoneNumber()
                2 -> return loginWithEmailId()
                3 -> signUp()
                0 -> return null
                -1 -> throw ExitApp()
                else -> ConsoleView.printInvalidOptions()
            }
        } while (true)

    }

    // login methods
    private fun loginWithPhoneNumber(): User? {

        do {

            ConsoleView.printHeader("Login With Phone Number")
            ConsoleView.printGoBackMessage()

            val phoneNumber = userView.getPhoneNumber()

            if(phoneNumber == "0") {
                return null
            }

            val password = userView.getPasswordForLogin()
            if(password == "0") {
                return null
            }

            try {
                return userService.loginWithPhoneNumber(phoneNumber, password)
            } catch (e: IllegalArgumentException) {
                ConsoleView.printError(e.message.toString())
            } catch (e: Exception) {
                ConsoleView.printHeader(e.message.toString())
            }

        }while (true)
    }
    private fun loginWithEmailId(): User? {

        do {

            ConsoleView.printHeader("Login With Email ID")
            ConsoleView.printGoBackMessage()

            val emailId = userView.getEmail()

            if(emailId == "0") {
                return null
            }

            val password = userView.getPasswordForLogin()
            if(password == "0") {
                return null
            }

            try {
                return userService.loginWithEmailId(emailId, password)
            } catch (e: IllegalArgumentException) {
                ConsoleView.printError(e.message.toString())
            } catch (e: Exception) {
                ConsoleView.printHeader(e.message.toString())
            }

        }while (true)
    }


    //sign up
    private fun signUp() {

        do {

            ConsoleView.printHeader("Sign Up")
            ConsoleView.printGoBackMessage()

            val name = userView.getName()
            if (name == "0") return
            val age = userView.getAge()
            if (age == 0.toByte()) return
            val email = userView.getEmail()
            if (email == "0") return
            val phoneNumber = userView.getPhoneNumber()
            if (phoneNumber == "0") return
            val location = userView.getLocation()
            if (location == "0") return
            val password = userView.getPasswordForSignUp()
            if (password == "0") return

            try {
                userService.createUser(name, email, phoneNumber, age, location, password)
                userView.showSignUpSuccess()
                return
            } catch (e: java.lang.IllegalArgumentException) {
                ConsoleView.printMessage(e.message.toString())
            } catch (e: Exception) {
                ConsoleView.printMessage(e.message.toString())
            }

        } while (true)

    }

}