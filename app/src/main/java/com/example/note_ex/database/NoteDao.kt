package com.example.note_ex.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.note_ex.model.Note

@Dao
interface NoteDao {

    /**
     * Insert a note in the database. If the note already exists, replace it.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM NOTES_TABLE ORDER BY id DESC")
    fun getAllNote() : LiveData<List<Note>>

    @Query("SELECT * FROM NOTES_TABLE WHERE note_title LIKE :query OR note_content LIKE :query")
    fun searchNote(query: String?): LiveData<List<Note>>
}