package daos

import daos.interfaces.UserDAO
import models.User

class UserDAOImp: UserDAO {
    override fun addUser(user: User) {
        DataStore.users[user.userId] = user
    }

    override fun getUserById(userId: String): User? {
        return DataStore.users[userId]
    }

    override fun getUserByPhoneNumber(phoneNumber: String): User? {
        for(user in DataStore.users.values) {
            if(user.userPhoneNumber.equals(phoneNumber, true)) {
                return user
            }
        }
        return null
    }

    override fun getUserByEmailId(email: String): User? {
        for(user in DataStore.users.values) {
            if(user.userEmail.equals(email, true)) {
                return user
            }
        }
        return null
    }

    override fun isEmailAvailable(email: String): Boolean = getUserByEmailId(email) == null

    override fun isPhoneNumberAvailable(phoneNumber: String): Boolean = getUserByPhoneNumber(phoneNumber) == null

}