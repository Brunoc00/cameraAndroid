package com.example.myapplication.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.FotoRepository
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.Foto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FotoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FotoRepository = FotoRepository(
        AppDatabase.getDatabase(application).fotoDao()
    )

    val fotos: StateFlow<List<Foto>> = repository.listar()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _fotoUriTemporaria = MutableStateFlow<Uri?>(null)
    val fotoUriTemporaria: StateFlow<Uri?> = _fotoUriTemporaria

    fun definirUriTemporaria(uri: Uri?) {
        _fotoUriTemporaria.value = uri
    }

    fun salvarFoto(uri: Uri) {
        viewModelScope.launch {
            repository.inserir(Foto(uri = uri.toString()))
            _fotoUriTemporaria.value = null
        }
    }
}