package com.example.restaurapp.ui.screens.profileScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.restaurapp.R
import com.example.restaurapp.viewmodel.AuthViewModel

@Composable
fun ProfileScreenBase(
    modifier: Modifier = Modifier,
    vm: AuthViewModel,
    onGoToEdit: () -> Unit,
    onLogoutClick: () -> Unit,
    horizontalPadding: Dp,
    spaceBeforeAvatar: Dp,
    avatarSize: Dp,
    nameTextStyle: TextStyle,
    emailTextStyle: TextStyle,
    spaceAfterAvatar: Dp,
    cardAndButtonWidthFraction: Float
) {
    val uiState by vm.uiState.collectAsState()
    val currentUser = uiState.currentUser

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .padding(horizontal = horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(spaceBeforeAvatar))

        // --- SECCIÓN DE AVATAR Y NOMBRE ---
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(cardAndButtonWidthFraction)
        ) {
            // CAMBIO 1: Usar AsyncImage para mostrar la foto del usuario
            AsyncImage(
                model = currentUser?.photoUrl,
                contentDescription = "Avatar de usuario",
                placeholder = painterResource(id = R.drawable.ic_default_avatar),
                error = painterResource(id = R.drawable.ic_default_avatar),
                modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentScale = ContentScale.Crop
            )

            // CAMBIO 2: Unir nombres y apellidos para mostrar el nombre completo
            Text(
                text = if (currentUser != null) "${currentUser.nombres} ${currentUser.apellidos}" else "Usuario Invitado",
                style = nameTextStyle,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = currentUser?.correo ?: "Sin correo electrónico",
                style = emailTextStyle,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(spaceAfterAvatar))

        // --- SECCIÓN DE OPCIONES (sin cambios) ---
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(cardAndButtonWidthFraction)
        ) {
            // Opción 1: Editar Perfil
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = onGoToEdit,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Editar Perfil",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Opción 2: Configuración
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = { /* TODO: Navegar a pantalla de configuración */ },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Configuración",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- BOTÓN CERRAR SESIÓN (sin cambios) ---
        Button(
            onClick = {
                vm.logout()
                onLogoutClick()
            },
            modifier = Modifier
                .fillMaxWidth(cardAndButtonWidthFraction)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = "Cerrar sesión"
                )
                Text(
                    "Cerrar Sesión",
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
