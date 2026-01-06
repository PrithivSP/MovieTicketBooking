package controllers


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
        return when (val authChoice: NavResult<Int> = userView.getAuthChoiceView()) {
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
    fun login(): AppFlowState {
        ConsoleView.printHeader("Login")

        return when (val choice = userView.getLoginChoice()) {
            NavResult.Back -> AppFlowState.AUTH_MENU
            NavResult.Exit -> AppFlowState.EXIT
            NavResult.Retry -> AppFlowState.LOGIN

            is NavResult.Result -> {
                when (choice.result) {
                    1 -> loginWithPhoneNumber()
                    2 -> loginWithEmailId()
                    else -> AppFlowState.LOGIN
                }
            }
        }
    }

    // login methods
    private fun loginWithPhoneNumber(): AppFlowState {

        ConsoleView.printHeader("Login with Phone Number")

        val phone = when (val r = userView.getPhoneNumber()) {
            NavResult.Back -> return AppFlowState.LOGIN
            NavResult.Exit -> return AppFlowState.EXIT
            NavResult.Retry -> return AppFlowState.LOGIN
            is NavResult.Result -> r.result
        }

        val password = when (val r = userView.getPasswordForLogin()) {
            NavResult.Back -> return AppFlowState.LOGIN
            NavResult.Exit -> return AppFlowState.EXIT
            NavResult.Retry -> return AppFlowState.LOGIN
            is NavResult.Result -> r.result
        }

        try {
            val user = userService.loginWithPhoneNumber(phone, password) ?: return AppFlowState.LOGIN
            Session.login(user)
            userView.showLoginSuccess(user.userName)
            return AppFlowState.USER_MENU
        } catch (e: Exception) {
            ConsoleView.printMessage(e.message ?: "Login failed")
            return AppFlowState.LOGIN
        }
    }

    private fun loginWithEmailId(): AppFlowState {
        ConsoleView.printHeader("Login with Email ID")
        val emailId = when (val r = userView.getEmail()) {
            NavResult.Back -> return AppFlowState.LOGIN
            NavResult.Exit -> return AppFlowState.EXIT
            NavResult.Retry -> return AppFlowState.LOGIN
            is NavResult.Result -> r.result
        }

        val password = when (val r = userView.getPasswordForLogin()) {
            NavResult.Back -> return AppFlowState.LOGIN
            NavResult.Exit -> return AppFlowState.EXIT
            NavResult.Retry -> return AppFlowState.LOGIN
            is NavResult.Result -> r.result
        }

        try {
            val user = userService.loginWithEmailId(emailId, password) ?: return AppFlowState.LOGIN
            Session.login(user)
            userView.showLoginSuccess(user.userName)
            return AppFlowState.USER_MENU
        } catch (e: Exception) {
            ConsoleView.printMessage(e.message ?: "Login failed")
            return AppFlowState.LOGIN
        }

    }

    //sign up
    fun signUp(): AppFlowState {

        ConsoleView.printHeader("Sign Up")
        ConsoleView.printGoBackMessage()

        val name = when (val r = userView.getName()) {
            NavResult.Back -> return AppFlowState.AUTH_MENU
            NavResult.Exit -> return AppFlowState.EXIT
            NavResult.Retry -> return AppFlowState.SIGNUP
            is NavResult.Result -> r.result
        }

        val age = when (val r = userView.getAge()) {
            NavResult.Back -> return AppFlowState.AUTH_MENU
            NavResult.Exit -> return AppFlowState.EXIT
            NavResult.Retry -> return AppFlowState.SIGNUP
            is NavResult.Result -> r.result
        }

        val email = when (val r = userView.getEmail()) {
            NavResult.Back -> return AppFlowState.AUTH_MENU
            NavResult.Exit -> return AppFlowState.EXIT
            NavResult.Retry -> return AppFlowState.SIGNUP
            is NavResult.Result -> r.result
        }

        val phoneNumber = when (val r = userView.getPhoneNumber()) {
            NavResult.Back -> return AppFlowState.AUTH_MENU
            NavResult.Exit -> return AppFlowState.EXIT
            NavResult.Retry -> return AppFlowState.SIGNUP
            is NavResult.Result -> r.result
        }

        val location = when (val r = userView.getLocation()) {
            NavResult.Back -> return AppFlowState.AUTH_MENU
            NavResult.Exit -> return AppFlowState.EXIT
            NavResult.Retry -> return AppFlowState.SIGNUP
            is NavResult.Result -> r.result
        }

        val password = when (val r = userView.getPasswordForSignUp()) {
            NavResult.Back -> return AppFlowState.AUTH_MENU
            NavResult.Exit -> return AppFlowState.EXIT
            NavResult.Retry -> return AppFlowState.SIGNUP
            is NavResult.Result -> r.result
        }

        return try {
            userService.createUser(name, email, phoneNumber, age, location, password)
            userView.showSignUpSuccess()
            AppFlowState.AUTH_MENU
        } catch (e: Exception) {
            ConsoleView.printMessage(e.message.toString())
            AppFlowState.SIGNUP
        }

    }

    //changing user location
    fun changeUserLocation(userID: String): AppFlowState {
        return when (val newUserLocation = userView.getLocation()) {

            NavResult.Back -> AppFlowState.USER_MENU
            NavResult.Exit -> AppFlowState.USER_MENU
            NavResult.Retry -> AppFlowState.CHANGE_LOCATION

            is NavResult.Result -> {
                userService.updateUserLocation(userID, newUserLocation.result)
                AppFlowState.USER_MENU
            }
        }

    }

    // updateUserProfile
    fun updateUserProfile(user: User): AppFlowState {
        ConsoleView.printHeader("Update Profile")
        return when (val choice = userView.showUpdateMenuAndGetChoice(user)) {

            is NavResult.Result -> {
                when (choice.result) {

                    // name
                    1 -> {
                        val newName = when (val r = userView.getName()) {
                            NavResult.Back -> return AppFlowState.UPDATE_PROFILE
                            NavResult.Exit -> return AppFlowState.USER_MENU
                            NavResult.Retry -> return AppFlowState.UPDATE_PROFILE
                            is NavResult.Result -> r.result
                        }
                        userService.updateUserName(user.userId, newName)
                    }

                    //phone number
                    2 -> {
                        val newPhoneNumber = when (val r = userView.getPhoneNumber()) {
                            NavResult.Back -> return AppFlowState.UPDATE_PROFILE
                            NavResult.Exit -> return AppFlowState.USER_MENU
                            NavResult.Retry -> return AppFlowState.UPDATE_PROFILE
                            is NavResult.Result -> r.result
                        }
                        userService.updateUserPhoneNumber(user.userId, newPhoneNumber)
                    }

                    // email id
                    3 -> {
                        val newEmail = when (val r = userView.getEmail()) {
                            NavResult.Back -> return AppFlowState.UPDATE_PROFILE
                            NavResult.Exit -> return AppFlowState.USER_MENU
                            NavResult.Retry -> return AppFlowState.UPDATE_PROFILE
                            is NavResult.Result -> r.result
                        }

                        userService.updateUserEmail(user.userId, newEmail)
                    }

                    //user location
                    4 -> {
                        return changeUserLocation(user.userId)
                    }

                    // password
                    5 -> {
                        val oldPassword = when (val r = userView.getCurrentPassword()) {
                            NavResult.Back -> return AppFlowState.UPDATE_PROFILE
                            NavResult.Exit -> return AppFlowState.USER_MENU
                            NavResult.Retry -> return AppFlowState.UPDATE_PROFILE
                            is NavResult.Result -> r.result
                        }

                        if (!user.verifyPassword(oldPassword)) {
                            userView.showPasswordDoNotMatch()
                            return AppFlowState.UPDATE_PROFILE
                        }

                        val newPassword = when (val r = userView.getPasswordForLogin()) {
                            NavResult.Back -> return AppFlowState.UPDATE_PROFILE
                            NavResult.Exit -> return AppFlowState.USER_MENU
                            NavResult.Retry -> return AppFlowState.UPDATE_PROFILE
                            is NavResult.Result -> r.result
                        }

                        userService.updateUserPassword(user.userId, newPassword)
                    }
                }

                userView.showUserProfileUpdated()
                AppFlowState.USER_MENU
            }

            NavResult.Back -> AppFlowState.USER_MENU
            NavResult.Exit -> AppFlowState.USER_MENU
            NavResult.Retry -> AppFlowState.UPDATE_PROFILE
        }
    }

}