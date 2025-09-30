package com.example.note_ex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.note_ex.database.NoteDatabase.Companion.DATABASE_VERSION
import com.example.note_ex.database.NoteDatabase.Companion.EXPORT_SCHEMA
import com.example.note_ex.model.Note

@Database(entities = [Note::class], version = DATABASE_VERSION, exportSchema = EXPORT_SCHEMA)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object {
        const val DATABASE_VERSION = 1
        const val EXPORT_SCHEMA = false

        private var instance: NoteDatabase? = null
        private var LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        /**
         * Tạo 1 hàm tạo Database (truyền context vào)
         * Dùng Room.databaseBuilder để tạo Database
         * Truyền vào các thuoojc tính là context, class Database và tên Database
         * Cuối cùng là .fallbackToDestructiveMigration() để migration khi có sự thay đổi cấu trúc Database
         */
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "note_db"
            ).fallbackToDestructiveMigration().build()
    }

}