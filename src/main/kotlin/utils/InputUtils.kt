package utils

import controllers.exceptions.ExitToUserMenu


fun readInt(prompt: String): Int {
    do {
        print(prompt)
        try {
            val input = readln().trim()
            if (input.isEmpty()) {
                println("Input cannot be empty. Please try again")
            } else if(input.toInt() == -1) {
                throw ExitToUserMenu()
            } else {
                return input.toInt()
            }
        } catch (e: NumberFormatException) {
            println("Invalid choice. Please enter a valid integer")
        }
    } while (true)
}

fun readString(prompt: String): String {
    do {
        print(prompt)
        val input = readln().trim()

        if(input.isEmpty()) {
            println("Input cannot be empty. Please try again")
        } else {
            return input
        }

    } while (true)
}

fun readPasswordForLogin(prompt: String): String {
    do {
        print(prompt)
        val input = readln().trim()

        if(input.isEmpty()) {
            println("Password cannot be empty. Please try again")
        } else {
            return input
        }

    } while (true)
}

fun readPasswordForSignUp(prompt: String): String {
    do {
        print(prompt)
        val password = readln().trim()

        if (password.isEmpty()) {
            println("Password cannot be empty. Please try again")
            continue
        }

        print("Re-enter password: ")
        val rePassword = readln().trim()

        if (rePassword.isEmpty()) {
            println("Password cannot be empty. Please try again")
            continue
        }

        if (password != rePassword) {
            println("Passwords do not match. Please try again.")
        } else {
            return password
        }

    } while (true)
}