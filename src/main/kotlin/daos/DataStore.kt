package daos

import models.User
import java.util.UUID

internal object DataStore {
    val users: MutableMap<String, User> = mutableMapOf()

    init {
        addSampleData()
    }

    private fun addSampleData() {
        println("Seeding sample data...")
        val u1 = User(
            UUID.randomUUID().toString(),
            "Max",
            "max@zoho.com",
            "9786337697",
            25, "chennai",
            "max12345",
            mutableListOf()
        )

        val u2 = User(
            UUID.randomUUID().toString(),
            "Holly",
            "holly@zoho.com",
            "0987654321",
            18, "chennai",
            "holly1234",
            mutableListOf()
        )

        users.put(u1.userId, u1)
        users.put(u2.userId, u2)
    }
}