package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_materials")
data class StudyMaterial(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subject: String,
    val type: String, // "NOTES", "PDF", "QUESTION", "PYQ"
    val content: String,
    val pdfUrl: String? = null,
    val year: Int? = null, // for PYQ
    val isFavorite: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),
    val branch: String = "Computer Science",
    val semester: String = "Semester 1",
    val description: String = ""
)
