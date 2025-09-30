package com.example.note_ex.repository

import androidx.room.Query
import com.example.note_ex.database.NoteDatabase
import com.example.note_ex.model.Note

class NoteRepository(private val notedb: NoteDatabase) {

    suspend fun insertNote(note: Note) = notedb.getNoteDao().insertNote(note)
    suspend fun updateNote(note: Note) = notedb.getNoteDao().updateNote(note)
    suspend fun deleteNote(note: Note) = notedb.getNoteDao().deleteNote(note)

    /**
     * Tại sao đoạn getAllNotes và searchNote phải dùng suspend fun
     * Vì các hàm này trả về LiveData, LiveData không phải là 1 giá trị
     *
     */

    fun getAllNotes() = notedb.getNoteDao().getAllNote()
    fun searchNote(query: String?) = notedb.getNoteDao().searchNote(query)
}