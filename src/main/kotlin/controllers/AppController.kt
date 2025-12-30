package controllers

import models.User
import services.UserServiceImp
import services.interfaces.UserService
import views.ConsoleView
import views.UserView

class AppController {
    fun start() {

        //service
        val userService: UserService = UserServiceImp()

        //view
        val userView: UserView = UserView()

        //controllers
        val userController: UserController = UserController(userService, userView)

        do {
            try {
                val user: User? = userController.authentication()
                if(user != null) {
                    println("Logging in...")
                    Session.login(user)
                    break
                }
            } catch (e: Exception) {
                ConsoleView.printMessage(e.message.toString())
            }

        } while (true)

        println("End")
    }
}