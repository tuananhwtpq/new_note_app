package com.example.note_ex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.example.note_ex.model.Note
import com.example.note_ex.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(private val repo: NoteRepository) : ViewModel() {

    private val _searchQuery = MutableLiveData<String?>(null)

    val allNotes: LiveData<List<Note>> = _searchQuery.switchMap { query ->
        if (query.isNullOrEmpty()){
            repo.getAllNotes()
        } else{
            repo.searchNote("%$query%")
        }
    }


    fun addNote(note: Note) = viewModelScope.launch {
        repo.insertNote(note)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        repo.updateNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        repo.deleteNote(note)
    }

    fun searchNote(query: String?) {
        _searchQuery.value = query
    }

    fun clearSeach(){
        _searchQuery.value = null
    }

}