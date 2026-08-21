package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fotos")
data class Foto(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val uri: String,

    val enviada: Boolean = false,

    val dataCriacao: Long = System.currentTimeMillis()
)