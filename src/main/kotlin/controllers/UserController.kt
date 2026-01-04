package controllers

import controllers.exceptions.AuthenticationException
import controllers.exceptions.ExitApp
import controllers.exceptions.ValidationException
import models.User
import models.enumerations.AppFlowState
import services.UserServiceImp
import services.interfaces.UserService
import views.ConsoleView
import views.UserView

class UserController(private val userService: UserService = UserServiceImp(), private val userView: UserView = UserView()) {

    // authentication
    fun showAuthenticationMenu(): AppFlowState {
            val authChoice: Int = userView.getAuthChoiceView()

            return when (authChoice) {
                1 -> AppFlowState.LOGIN
                2 -> AppFlowState.SIGNUP
                0, -1 -> throw ExitApp("Thanks for choosing us...")
                else -> {
                    ConsoleView.printInvalidOptions()
                    AppFlowState.AUTH_MENU
                }
            }
    }

    // logging in
    fun login(): User? {

        do {

            ConsoleView.printHeader("Login")
            val loginChoice = userView.getLoginChoice()

            when (loginChoice) {
                1 -> {
                    // login with user phone number -> if null user pressed 0
                    val user = loginWithPhoneNumber()
                    if (user == null) {
                        continue
                    } else {
                        return user
                    }
                }
                2 -> {
                    // login with user phone number -> if null user pressed 0
                    val user = loginWithEmailId()
                    if (user == null) {
                        continue
                    } else {
                        return user
                    }
                }
                3 -> signUp()
                0 -> return null
                -1 -> throw ExitApp("Thanks for choosing us...")
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

            if (phoneNumber == "0") {
                return null
            }

            val password = userView.getPasswordForLogin()
            if (password == "0") {
                return null
            }

            try {
                return userService.loginWithPhoneNumber(phoneNumber, password)
            }
            catch (e: ValidationException) {
                ConsoleView.printMessage(e.message.toString())
            }
            catch (e: AuthenticationException) {
                ConsoleView.printMessage(e.message.toString())
            }
            catch (e: IllegalArgumentException) {
                ConsoleView.printError(e.message.toString())
            }
            catch (e: Exception) {
                ConsoleView.printError(e.message.toString())
            }

        } while (true)
    }
    private fun loginWithEmailId(): User? {

        do {

            ConsoleView.printHeader("Login With Email ID")
            ConsoleView.printGoBackMessage()

            val emailId = userView.getEmail()

            if (emailId == "0") {
                return null
            }

            val password = userView.getPasswordForLogin()
            if (password == "0") {
                return null
            }

            try {
                return userService.loginWithEmailId(emailId, password)
            }
            catch (e: ValidationException) {
                ConsoleView.printMessage(e.message.toString())
            }
            catch (e: AuthenticationException) {
                ConsoleView.printMessage(e.message.toString())
            }
            catch (e: IllegalArgumentException) {
                ConsoleView.printError(e.message.toString())
            }
            catch (e: Exception) {
                ConsoleView.printMessage(e.message.toString())
            }

        } while (true)
    }


    //sign up
    fun signUp() {

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
            }
            catch (e: ValidationException) {
                ConsoleView.printMessage(e.message.toString())
            }
            catch (e: IllegalArgumentException) {
                ConsoleView.printMessage(e.message.toString())
            }
            catch (e: Exception) {
                ConsoleView.printMessage(e.message.toString())
            }

        } while (true)

    }

}