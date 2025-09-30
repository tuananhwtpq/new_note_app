package com.example.note_ex.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "notes_table")
@Parcelize
data class Note (

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val note_title: String,
    val note_content: String,
    val dateTime: String
) : Parcelable