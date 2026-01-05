package controllers

import controllers.exceptions.ValidationException
import models.User
import models.dtos.NavResult
import models.enumerations.AppFlowState
import services.UserServiceImp
import services.interfaces.UserService
import views.ConsoleView
import views.UserView

class UserController(
    private val userService: UserService = UserServiceImp(),
    private val userView: UserView = UserView()
) {

    // authentication
    fun showAuthenticationMenu(): AppFlowState {

        ConsoleView.printHeader("Movie Ticket Booking App")
        val authChoice: NavResult<Int> = userView.getAuthChoiceView()

        return when (authChoice) {
            NavResult.Back -> AppFlowState.AUTH_MENU

            NavResult.Exit -> AppFlowState.EXIT

            NavResult.Retry -> AppFlowState.AUTH_MENU

            is NavResult.Result -> when (authChoice.result) {
                1 -> AppFlowState.LOGIN
                2 -> AppFlowState.SIGNUP
                0 -> AppFlowState.EXIT
                else -> AppFlowState.AUTH_MENU
            }
        }
    }

    // logging in
    fun login(): NavResult<User?> {

        do{
            ConsoleView.printHeader("Login")

            when(val choice = userView.getLoginChoice()) {
                NavResult.Back -> return NavResult.Back
                NavResult.Exit -> return NavResult.Exit
                NavResult.Retry -> continue

                is NavResult.Result -> {
                    return when (choice.result) {
                        1 -> {
                            val result = loginWithPhoneNumber()
                            if(result == NavResult.Back) {
                                continue
                            } else {
                                result
                            }
                        }
                        2 ->
                        {
                            val result = loginWithEmailId()
                            if(result == NavResult.Back) {
                                continue
                            } else {
                                result
                            }
                        }

                        else -> NavResult.Retry
                    }
                }
            }
        } while (true)

    }

    // login methods
    private fun loginWithPhoneNumber(): NavResult<User?> {

        ConsoleView.printHeader("Login with Phone Number")

        when(val phone = userView.getPhoneNumber()) {
            NavResult.Back -> return NavResult.Back
            NavResult.Exit -> return NavResult.Exit
            NavResult.Retry -> return NavResult.Retry

            is NavResult.Result -> {
                when(val password = userView.getPasswordForLogin()) {
                    NavResult.Back -> return NavResult.Back
                    NavResult.Exit -> return NavResult.Exit
                    NavResult.Retry -> return NavResult.Retry

                    is NavResult.Result -> {
                        return try {
                            NavResult.Result(
                                userService.loginWithPhoneNumber(
                                    phone.result,
                                    password.result
                                )
                            )
                        } catch (e: Exception) {
                            ConsoleView.printMessage(e.message ?: "Login failed")
                            NavResult.Retry
                        }
                    }
                }
            }
        }
    }

    private fun loginWithEmailId(): NavResult<User?> {
        ConsoleView.printHeader("Login with Email ID")
        when (val email = userView.getEmail()) {
            NavResult.Back -> return NavResult.Back
            NavResult.Exit -> return NavResult.Exit
            NavResult.Retry -> return NavResult.Retry

            is NavResult.Result -> {
                when (val password = userView.getPasswordForLogin()) {
                    NavResult.Back -> return NavResult.Back
                    NavResult.Exit -> return NavResult.Exit
                    NavResult.Retry -> return NavResult.Retry

                    is NavResult.Result -> {
                        return try {
                            NavResult.Result(
                                userService.loginWithEmailId(
                                    email.result,
                                    password.result
                                )
                            )
                        } catch (e: Exception) {
                            ConsoleView.printMessage(e.message ?: "Login failed")
                            NavResult.Retry
                        }
                    }
                }
            }
        }
    }


    //sign up
    fun signUp(): NavResult<Unit> {

        do {

            ConsoleView.printHeader("Sign Up")
            ConsoleView.printGoBackMessage()

            val name = when (val r = userView.getName()) {
                NavResult.Back -> return NavResult.Back
                NavResult.Exit -> return NavResult.Exit
                NavResult.Retry -> continue
                is NavResult.Result -> r.result
            }
            val age = when (val r = userView.getAge()) {
                NavResult.Back -> return NavResult.Back
                NavResult.Exit -> return NavResult.Exit
                NavResult.Retry -> continue
                is NavResult.Result -> r.result
            }

            val email = when (val r = userView.getEmail()) {
                NavResult.Back -> return NavResult.Back
                NavResult.Exit -> return NavResult.Exit
                NavResult.Retry -> continue
                is NavResult.Result -> r.result
            }

            val phoneNumber = when (val r = userView.getPhoneNumber()) {
                NavResult.Back -> return NavResult.Back
                NavResult.Exit -> return NavResult.Exit
                NavResult.Retry -> continue
                is NavResult.Result -> r.result
            }
            val location = when (val r = userView.getLocation()) {
                NavResult.Back -> return NavResult.Back
                NavResult.Exit -> return NavResult.Exit
                NavResult.Retry -> continue
                is NavResult.Result -> r.result
            }

            val password = when (val r = userView.getPasswordForSignUp()) {
                NavResult.Back -> return NavResult.Back
                NavResult.Exit -> return NavResult.Exit
                NavResult.Retry -> continue
                is NavResult.Result -> r.result
            }

            try {
                userService.createUser(name, email, phoneNumber, age, location, password)
                userView.showSignUpSuccess()
                return NavResult.Result(Unit)
            } catch (e: ValidationException) {
                ConsoleView.printMessage(e.message.toString())
            } catch (e: IllegalArgumentException) {
                ConsoleView.printMessage(e.message.toString())
            } catch (e: Exception) {
                ConsoleView.printMessage(e.message.toString())
            }

        } while (true)

    }

}