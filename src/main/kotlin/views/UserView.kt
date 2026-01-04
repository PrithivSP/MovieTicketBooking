package views

import utils.isValidAge
import utils.isValidEmailId
import utils.isValidPhoneNumber
import utils.readInt
import utils.readPasswordForLogin
import utils.readPasswordForSignUp
import utils.readString

class UserView {
    fun getAuthChoiceView(): Int {
        println("1. Login")
        println("2. SignUp")
        println("0. Exit")
        return readInt("Enter your choice (or 0 to quit): ")
    }

    fun getLoginChoice(): Int {
        println("1. Login with phone number")
        println("2. Login with email id")
        println("0. Exit")
        return readInt("Enter your choice (or 0 to back): ")
    }

    fun getName(): String {
        return readString("Enter your name: ")
    }

    fun getAge(): Byte {
        return readInt("Enter your age: ").toByte()

    }

    fun getLocation(): String {
        return readString("Enter your location: ")
    }

    fun getEmail(): String {
        return readString("Enter your email: ")

    }

    fun getPhoneNumber(): String {
        return readString("Enter your phone number: ")
    }

    fun getPasswordForSignUp(): String {
        return readPasswordForSignUp("Enter your password: ")
    }

    fun getPasswordForLogin(): String {
        return readPasswordForLogin("Enter your password: ")
    }

    fun showNoUserLoggedIn() {
        println("Please Login to continue...")
    }

    fun showSignUpSuccess() {
        println("Signed up successful. Please proceed with login")
    }

    fun getUpdateMenuChoice(): Int {
        return readInt("Enter your choice (or 0 to go back): ")
    }

    fun showUpdateMenu(name: String?, phoneNumber: String?, email: String?, location: String?) {
        println("Current details:")
        println("1) Name     : $name")
        println("2) Phone    : $phoneNumber")
        println("3) Email    : $email")
        println("4) Location : $location")
        println("5) Change Password")
        println("0) Back")
    }

    fun showPasswordDoNotMatch() {
        println("Password do not match. Try again")
    }

    fun showLoginSuccess(userName: String?) {
        println("Logged in. Welcome $userName!")
    }

}