package views

import utils.readInt

class BrowsingView {

    fun getUserMenuChoice(): Int {
        ConsoleView.printHeader("User Menu")
        println("1. Browse movies")
        println("2. Browse movie by name")
        println("3. Browse near by theaters")
        println("4. Browse genres")
        println("5. Change location")
        println("6. Show booking history")
        println("7. Cancel a booking")
        println("8. Update profile")
        println("0. Logout")
        return readInt("Enter your choice (or 0 to go back): ")
    }

}