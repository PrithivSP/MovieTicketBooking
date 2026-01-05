package views

import models.Booking
import models.dtos.BookingDisplay
import models.dtos.NavResult
import utils.isValidAge
import utils.isValidEmailId
import utils.isValidPhoneNumber
import utils.readInt
import utils.readPasswordForLogin
import utils.readPasswordForSignUp
import utils.readString

class UserView {
    fun getAuthChoiceView(): NavResult<Int> {
        println("1. Login")
        println("2. SignUp")
        println("0. Exit")
        return when(val input = readInt("Enter your choice (or 0 to quit): ")) {
            0 -> NavResult.Exit
            -1 -> NavResult.Exit
            1, 2 -> NavResult.Result(input)
            else -> {
                ConsoleView.printInvalidOptions()
                NavResult.Retry
            }
        }
    }

    fun getLoginChoice(): NavResult<Int> {
        println("1. Login with phone number")
        println("2. Login with email id")
        println("0. Exit")
        return when(val input = readInt("Enter your choice (or 0 to back): ")) {
            0 -> NavResult.Back
            -1 -> NavResult.Exit
            1, 2 -> NavResult.Result(input)
            else -> {
                ConsoleView.printInvalidOptions()
                NavResult.Retry
            }
        }
    }

    fun getName(): NavResult<String> {
        val input = readString("Enter name: ").trim()

        return when {
            input == "-1" -> NavResult.Exit
            input == "0" -> NavResult.Back
            else -> NavResult.Result(input)
        }
    }

    fun getAge(): NavResult<Byte> {
        do {
            val age = readInt("Enter your age: ")

            return when {
                age == -1 -> NavResult.Exit
                age == 0 -> NavResult.Back
                !age.toByte().isValidAge() -> {
                    ConsoleView.printInputNotValid("age")
                    continue
                }
                else -> NavResult.Result(age.toByte())
            }
        } while (true)
    }

    fun getLocation(): NavResult<String> {
        val location = readString("Enter your location: ").trim()

        return when {
            location == "0" -> NavResult.Back
            location == "-1" -> NavResult.Exit
            else -> NavResult.Result(location)
        }
    }

    fun getEmail(): NavResult<String> {

        do {
            val emailId = readString("Enter your email: ")
            return when {
                emailId == "0" -> NavResult.Back
                emailId == "-1" -> NavResult.Exit
                !emailId.isValidEmailId() -> {
                    ConsoleView.printInputNotValid("Email ID")
                    continue
                }
                else -> {
                    NavResult.Result(emailId)
                }
            }
        } while (true)
    }

    fun getPhoneNumber(): NavResult<String> {

        do {
            val phoneNumber = readString("Enter your phone number: ")
            return when {
                phoneNumber == "0" -> NavResult.Back
                phoneNumber == "-1" -> NavResult.Exit
                !phoneNumber.isValidPhoneNumber() -> {
                    ConsoleView.printInputNotValid("Phone Number")
                    continue
                }
                else -> {
                    NavResult.Result(phoneNumber)
                }
            }
        } while (true)
    }

    fun getPasswordForSignUp(): NavResult<String> {
        val password = readPasswordForSignUp("Enter password: ")

        return when {
            password == "-1" -> NavResult.Exit
            password == "0" -> NavResult.Back
            else -> NavResult.Result(password)
        }
    }

    fun getPasswordForLogin(): NavResult<String> {
        val password = readPasswordForLogin("Enter password: ")
        return when {
            password == "-1" -> NavResult.Exit
            password == "0" -> NavResult.Back
            else -> NavResult.Result(password)
        }
    }

    fun showSignUpSuccess() {
        println("Signed up successful. Please proceed with login")
    }

    fun showNoUserLoggedIn() {
        println("Please Login to continue...")
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

    fun showNoBookingsFound() {
        println("\nNo bookings found for this user")
    }

}