package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.myapplication.data.local.criarArquivoDeImagem
import com.example.myapplication.data.local.obterUriParaArquivo
import com.example.myapplication.ui.FotoViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TelaFotos(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TelaFotos(modifier: Modifier = Modifier, vm: FotoViewModel = viewModel()) {
    val context = LocalContext.current
    val fotos by vm.fotos.collectAsState()
    val uriTemporaria by vm.fotoUriTemporaria.collectAsState()

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { sucesso ->
        if (sucesso) {
            uriTemporaria?.let { vm.salvarFoto(it) }
        } else {
            vm.definirUriTemporaria(null) // usuário cancelou
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedida ->
        if (concedida) {
            val arquivo = criarArquivoDeImagem(context)
            val uri = obterUriParaArquivo(context, arquivo)
            vm.definirUriTemporaria(uri)
            cameraLauncher.launch(uri)
        }
    }

    fun iniciarCaptura() {
        val temPermissao = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (temPermissao) {
            val arquivo = criarArquivoDeImagem(context)
            val uri = obterUriParaArquivo(context, arquivo)
            vm.definirUriTemporaria(uri)
            cameraLauncher.launch(uri)
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = { iniciarCaptura() }) {
            Text("Tirar foto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Fotos salvas (${fotos.size})", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(fotos, key = { it.id }) { foto ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = foto.uri,
                        contentDescription = "Foto salva",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
            }
        }
    }
}