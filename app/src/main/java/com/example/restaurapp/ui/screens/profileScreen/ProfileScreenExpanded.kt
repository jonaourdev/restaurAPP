package com.example.restaurapp.ui.screens.profileScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.restaurapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenExpanded(
    vm: AuthViewModel,
    onLogoutClick: () -> Unit,
    onGoToEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileScreenBase(
        modifier = modifier,
        vm = vm,
        onGoToEdit = onGoToEdit,
        onLogoutClick = onLogoutClick,
        horizontalPadding = 128.dp,
        spaceBeforeAvatar = 64.dp,
        avatarSize = 180.dp,
        nameTextStyle = MaterialTheme.typography.headlineLarge,
        emailTextStyle = MaterialTheme.typography.titleLarge,
        spaceAfterAvatar = 80.dp,
        cardAndButtonWidthFraction = 0.6f
    )
}
