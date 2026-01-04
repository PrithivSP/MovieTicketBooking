package controllers

import models.enumerations.AppFlowState
import views.BrowsingView
import views.ConsoleView

class BrowsingController(
    val browsingView: BrowsingView = BrowsingView()
) {

    fun userMenu(): AppFlowState {
        ConsoleView.printHeader("User Menu")
        val userMenuChoice = browsingView.getUserMenuChoice()

        return when (userMenuChoice) {
            1 -> AppFlowState.BROWSE_MOVIES
            2 -> AppFlowState.SEARCH_MOVIE
            0, -1 -> AppFlowState.AUTH_MENU
            else -> {
                ConsoleView.printInvalidOptions()
                AppFlowState.USER_MENU
            }
        }
    }
}
