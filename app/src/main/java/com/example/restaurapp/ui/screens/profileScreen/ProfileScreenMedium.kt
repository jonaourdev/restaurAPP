package com.example.restaurapp.ui.screens.profileScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenMedium(
    vm: AuthViewModel,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileScreenBase(
        vm = vm,
        onLogoutClick = onLogoutClick,
        horizontalPadding = 64.dp,
        spaceBeforeAvatar = 48.dp,
        avatarSize = 150.dp,
        nameTextStyle = MaterialTheme.typography.headlineMedium,
        emailTextStyle = MaterialTheme.typography.titleMedium,
        spaceAfterAvatar = 64.dp,
        cardAndButtonWidthFraction = 0.8f
    )
}