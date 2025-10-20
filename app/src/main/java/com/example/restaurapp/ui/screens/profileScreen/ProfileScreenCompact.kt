package com.example.restaurapp.ui.screens.profileScreen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// Se corrige la importación, ya que este Composable usa ProfileScreenBase
import com.example.restaurapp.ui.screens.profileScreen.ProfileScreenBase
import com.example.restaurapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenCompact(
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
        horizontalPadding = 16.dp,
        spaceBeforeAvatar = 32.dp,
        avatarSize = 120.dp,
        nameTextStyle = MaterialTheme.typography.headlineSmall,
        emailTextStyle = MaterialTheme.typography.bodyLarge,
        spaceAfterAvatar = 48.dp,
        cardAndButtonWidthFraction = 1.0f
    )
}
