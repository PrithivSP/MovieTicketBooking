package controllers

import models.enumerations.AppFlowState
import views.BrowsingView
import views.ConsoleView

class BrowsingController(
    val browsingView: BrowsingView = BrowsingView()
) {

    fun userMenu(): AppFlowState {
        val userMenuChoice = browsingView.getUserMenuChoice()

        return when (userMenuChoice) {
            1 -> AppFlowState.BROWSE_MOVIES
            2 -> AppFlowState.SEARCH_MOVIE
            3 -> AppFlowState.SELECT_THEATER
            4 -> AppFlowState.CHANGE_LOCATION
            5 -> AppFlowState.SHOW_HISTORY
            6 -> AppFlowState.CANCEL_BOOKING
            7 -> AppFlowState.UPDATE_PROFILE
            0, -1 -> AppFlowState.AUTH_MENU
            else -> {
                ConsoleView.printInvalidOptions()
                AppFlowState.USER_MENU
            }
        }
    }
}
