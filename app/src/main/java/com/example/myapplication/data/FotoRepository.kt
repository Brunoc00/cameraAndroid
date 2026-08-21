package com.example.myapplication.data

import com.example.myapplication.data.local.Foto
import com.example.myapplication.data.local.FotoDao

class FotoRepository(
    private val fotoDao: FotoDao
) {

    suspend fun inserir(foto: Foto) {
        fotoDao.inserir(foto)
    }

    fun listar() = fotoDao.listar()

    suspend fun atualizar(foto: Foto) {
        fotoDao.atualizar(foto)
    }
}