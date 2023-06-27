package com.example.fragmentpractice3.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fragmentpractice3.dao.WordDao
import com.example.fragmentpractice3.datas.ReData

@Database(entities = [ReData::class], version = 3)
abstract class AppDatabase : RoomDatabase(){

    // WordDao인터페이스 들고오기
    abstract fun wordDao(): WordDao
    companion object{
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "app-database3.db"
                    ).build()
                }

            }
            return INSTANCE
        }
    }
}