package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyMaterialDao {
    @Query("SELECT * FROM study_materials ORDER BY timestamp DESC")
    fun getAllMaterials(): Flow<List<StudyMaterial>>

    @Query("SELECT * FROM study_materials WHERE id = :id LIMIT 1")
    fun getMaterialById(id: Int): Flow<StudyMaterial?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: StudyMaterial): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterials(materials: List<StudyMaterial>)

    @Update
    suspend fun updateMaterial(material: StudyMaterial)

    @Delete
    suspend fun deleteMaterial(material: StudyMaterial)
}
