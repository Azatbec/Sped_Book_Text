package com.example.sped_book_text.dao

import androidx.room.*
import com.example.sped_book_text.model.User

@Dao
interface UserDao {

    // Вставка пользователя с обработкой конфликта
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    // Получение пользователя по email
    @Query("SELECT * FROM User WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?

    // Получение пользователя по ID
    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun getUserById(id: Int): User?

    // Проверка, существует ли пользователь с таким email
    @Query("SELECT COUNT(*) FROM User WHERE email = :email")
    suspend fun isEmailExist(email: String): Boolean

    // Получение всех пользователей
    @Query("SELECT * FROM User")
    suspend fun getAllUsers(): List<User>

    // Обновление данных пользователя
    @Update
    suspend fun updateUser(user: User)

    // Обновление пароля пользователя по email
    @Query("UPDATE User SET password = :newPassword WHERE email = :email")
    suspend fun updatePasswordByEmail(email: String, newPassword: String)

    // Удаление пользователя
    @Delete
    suspend fun deleteUser(user: User)

    // Обновление аватара пользователя по id
    @Query("UPDATE User SET avatarUri = :avatarUri WHERE id = :userId")
    suspend fun updateAvatarUri(userId: Int, avatarUri: String)

    // Обновление данных пользователя и его аватара
    @Transaction
    suspend fun updateUserDataAndAvatar(user: User, avatarUri: String) {
        updateUser(user)
        updateAvatarUri(user.id, avatarUri)
    }
}
