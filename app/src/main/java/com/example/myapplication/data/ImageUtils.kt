package com.example.myapplication.data.local

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun criarArquivoDeImagem(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val nomeArquivo = "FOTO_${timeStamp}.jpg"
    val diretorio = context.getExternalFilesDir("Pictures")
    return File(diretorio, nomeArquivo)
}

fun obterUriParaArquivo(context: Context, arquivo: File): Uri {
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        arquivo
    )
}