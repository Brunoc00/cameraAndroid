package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FotoDao {

    @Insert
    suspend fun inserir(foto: Foto)

    @Query("SELECT * FROM fotos ORDER BY dataCriacao DESC")
    fun listar(): Flow<List<Foto>>

    @Update
    suspend fun atualizar(foto: Foto)
}