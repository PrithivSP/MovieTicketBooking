package daos.interfaces

import models.User

interface UserDAO {

    fun addUser(user: User)

    fun getUserById(userId: String): User?
    fun getUserByPhoneNumber(phoneNumber: String): User?
    fun getUserByEmailId(email: String): User?

}