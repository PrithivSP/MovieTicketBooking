package views

import models.Booking
import models.User
import models.dtos.BookingDisplay
import models.dtos.NavResult
import utils.isValidAge
import utils.isValidEmailId
import utils.isValidPassword
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
        return when (val input = readInt("Enter your choice (or 0 to quit): ")) {
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
        return when (val input = readInt("Enter your choice (or 0 to back): ")) {
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
        do {
            val password = readPasswordForSignUp("Enter password: ")

            return when {
                password == "-1" -> NavResult.Exit
                password == "0" -> NavResult.Back
                !password.isValidPassword() -> {
                    ConsoleView.printInputNotValid("Password")
                    println("Password should be at least 8 characters")
                    continue
                }
                else -> NavResult.Result(password)
            }
        } while (true)
    }

    fun getPasswordForLogin(): NavResult<String> {
        val password = readPasswordForLogin("Enter password: ")
        return when {
            password == "-1" -> NavResult.Exit
            password == "0" -> NavResult.Back
            else -> NavResult.Result(password)
        }
    }

    fun getCurrentPassword(): NavResult<String> {
        val password = readPasswordForLogin("\nEnter your current password: ")
        return when {
            password == "-1" -> NavResult.Exit
            password == "0" -> NavResult.Back
            else -> NavResult.Result(password)
        }
    }

    fun showSignUpSuccess() = println("\nSigned up successful. Please proceed with login")

//    fun showNoUserLoggedIn() {
//        println("Please Login to continue...")
//    }

    fun showUpdateMenuAndGetChoice(user: User): NavResult<Int> {
        println("Current details:")
        println("1) Name     : ${user.userName}")
        println("2) Phone    : ${user.userPhoneNumber}")
        println("3) Email    : ${user.userEmail}")
        println("4) Location : ${user.userLocation}")
        println("5) Change Password")
        println("0) Back")

        val input = readInt("Enter your choice (or 0 to go back): ")

        return when {
            input == -1 -> NavResult.Exit
            input == 0 -> NavResult.Back
            else -> NavResult.Result(input)
        }
    }

    fun showUserProfileUpdated() = println("\nUser Profile is updated...")

    fun showPasswordDoNotMatch() = println("\nPassword do not match. Try again")

    fun showLoginSuccess(userName: String?) = println("\nLogged in. Welcome $userName!")

    fun showNoBookingsFound() = println("\nNo bookings found for this user")

}