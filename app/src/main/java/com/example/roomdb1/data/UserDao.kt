// UserDao.kt
package com.example.roomdb1

import user
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: user)

    @Query("SELECT * FROM User")
    suspend fun getAllUsers(): List<user>
}
