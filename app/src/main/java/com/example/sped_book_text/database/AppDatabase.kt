package com.example.sped_book_text.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sped_book_text.dao.UserDao
import com.example.sped_book_text.model.User

@Database(entities = [User::class], version = 2)  // Обновляем версию базы данных
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "User"
                )
                    .fallbackToDestructiveMigration()  // Добавляем fallback для разрушения и пересоздания базы данных при обновлении версии
                    .build().also { INSTANCE = it }
            }
        }
    }
}
