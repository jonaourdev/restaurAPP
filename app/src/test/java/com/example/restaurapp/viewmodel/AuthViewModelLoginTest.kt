package com.example.restaurapp.viewmodel

import com.example.restaurapp.MainDispatcherRule
import com.example.restaurapp.model.local.user.UserEntity
import com.example.restaurapp.model.repository.AuthRepository
import com.example.restaurapp.model.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@ExperimentalCoroutinesApi
class AuthViewModelLoginTest {

    companion object {
        @JvmField
        @RegisterExtension
        val mainDispatcherRule = MainDispatcherRule()
    }

    // 1. Dependencias simuladas (mocks)
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository

    // 2. El ViewModel que vamos a probar
    private lateinit var authViewModel: AuthViewModel

    // 3. Un usuario de prueba
    private val testUser = UserEntity(
        id = 1,
        nombres = "Lucas",
        apellidos = "Test",
        correo = "lucas@test.com",
        contrasenna = "password123"
    )

    @BeforeEach
    fun setUp() {
        authRepository = mockk()
        userRepository = mockk(relaxed = true)
        authViewModel = AuthViewModel(authRepository, userRepository)
    }

    @Test
    fun `login DEBE ser exitoso CUANDO las credenciales son correctas`() = runTest {
        coEvery { authRepository.login("lucas@test.com", "password123") } returns testUser
        authViewModel.onLoginCorreoChange("lucas@test.com")
        authViewModel.onLoginContrasennaChange("password123")

        authViewModel.login()

        var currentState = authViewModel.uiState.value
        assertTrue(currentState.isLoading)
        assertNull(currentState.error)
        assertEquals(testUser, currentState.currentUser)

        advanceUntilIdle()

        currentState = authViewModel.uiState.value
        assertFalse(currentState.isLoading)
        assertTrue(currentState.success)
        assertEquals(testUser, currentState.currentUser)
    }

    @Test
    fun `login DEBE fallar CUANDO las credenciales son incorrectas`() = runTest {
        coEvery { authRepository.login("lucas@test.com", "wrongpassword") } returns null
        authViewModel.onLoginCorreoChange("lucas@test.com")
        authViewModel.onLoginContrasennaChange("wrongpassword")

        authViewModel.login()

        val currentState = authViewModel.uiState.value
        assertFalse(currentState.isLoading)
        assertFalse(currentState.success)
        assertNull(currentState.currentUser)
        assertEquals("Correo o contraseña inválidos.", currentState.error)
    }

    @Test
    fun `onLoginCorreoChange DEBE actualizar el estado del correo en AuthUiState`() {
        val testEmail = "test@example.com"
        authViewModel.onLoginCorreoChange(testEmail)
        assertEquals(testEmail, authViewModel.uiState.value.loginCorreo)
    }

    @Test
    fun `onLoginContrasennaChange DEBE actualizar el estado de la contraseña en AuthUiState`() {
        val testPassword = "my-secret-password"
        authViewModel.onLoginContrasennaChange(testPassword)
        assertEquals(testPassword, authViewModel.uiState.value.loginContrasenna)
    }
}
