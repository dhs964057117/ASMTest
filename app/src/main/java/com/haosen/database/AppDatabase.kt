package com.haosen.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.haosen.database.history.History
import com.haosen.database.history.HistoryDao
import com.haosen.database.user.User
import com.haosen.database.user.UserDao
import com.haosen.tools.base.provider.BaseContentProvider


@Database(entities = [History::class, User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private fun getDatabase() = INSTANCE ?: synchronized(AppDatabase::class.java) {
            INSTANCE ?: buildDatabase().also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context = BaseContentProvider.context()): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database"
            ).build()
        }

        @JvmStatic
        fun getHistoryDao(): HistoryDao {
            return getDatabase().historyDao()
        }

        @JvmStatic
        fun getUserDao(): UserDao {
            return getDatabase().userDao()
        }

        @JvmStatic
        fun closeDB() {
            getDatabase().close()
        }

    }

    override fun close() {
        super.close()
        //数据库关闭后把instance置空
        INSTANCE = null
    }

}