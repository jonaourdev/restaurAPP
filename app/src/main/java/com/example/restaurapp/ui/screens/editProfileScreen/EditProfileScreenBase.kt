package com.example.restaurapp.ui.screens.editProfileScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.restaurapp.R
import com.example.restaurapp.viewmodel.AuthViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreenBase(
    modifier: Modifier = Modifier,
    vm: AuthViewModel,
    onNavigateBack: () -> Unit,
    formWidth: Float
) {
    val uiState by vm.uiState.collectAsState()
    val context = LocalContext.current

    //Seleccionar imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri: Uri? ->
        uri?.let { vm.onProfileImageChange(it) }
    }

    //Navegar hacia atrás cuando la actualización sea exitosa
    LaunchedEffect(uiState.updateSuccess) {
        if (uiState.updateSuccess) {
            onNavigateBack()
            vm.resetUpdateStatus()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )

        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(formWidth)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                //Selección imagen de perfil
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { imagePickerLauncher.launch("image/*") }
                ) {
                    AsyncImage(
                        model = uiState.editProfileImageUri ?: uiState.currentUser?.photoUrl,
                        contentDescription = "Imagen de perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.ic_default_avatar),
                        error = painterResource(id = R.drawable.ic_default_avatar)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Cambiar", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }

                // FORMULARIO
                OutlinedTextField(
                    value = uiState.editNombreCompleto,
                    onValueChange = vm::onEditNombreChange,
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    enabled = !uiState.isLoading
                )

                //Mensaje de error
                uiState.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                //Botón para guardar
                Button(
                    onClick = { vm.actualizarPerfil() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        Text(text = "Guardar Cambios", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}