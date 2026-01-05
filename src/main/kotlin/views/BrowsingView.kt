package views

import utils.readInt

class BrowsingView {

    fun getUserMenuChoice(): Int {
        ConsoleView.printHeader("User Menu")
        println("1. Browse movies")
        println("2. Browse movie by name")
        println("3. Browse near by theaters")
        println("4. Change location")
        println("5. Show booking history")
        println("6. Cancel a booking")
        println("7. Update profile")
        println("0. Logout")
        return readInt("Enter your choice (or 0 to go back): ")
    }

}