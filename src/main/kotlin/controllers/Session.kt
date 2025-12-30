package controllers

import models.User

internal object Session {
    var currentUser: User? = null
    private set


    fun login(user: User) {
        currentUser = user
    }

    fun logout() {
        currentUser = null
    }

    fun isLoggedIn(): Boolean {
        return currentUser != null
    }
}