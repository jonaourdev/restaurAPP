package com.example.restaurapp.model.repository

import com.example.restaurapp.model.local.user.UserDao
import com.example.restaurapp.model.local.user.UserEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserRepositoryTest {
    // DAO simulado (mock) con MockK
    private val userDao: UserDao = mockk(relaxed = true)
    // Repositorio a probar, inyectándole el DAO simulado
    private lateinit var repository: UserRepository
    // objeto de usuario base para reutilizar en los tests
    private val baseUser = UserEntity(
        id = 1,
        nombres = "  Lucas  ",
        apellidos = "Prueba",
        correo = "  lucas.prueba@example.com  ",
        contrasenna = "12345678",
        photoUrl = null
    )

    // Clean state
    @BeforeEach
    fun setUp() {
        repository = UserRepository(userDao)
    }

    @Test
    fun `getAllUsers devuelve un Flow de usuarios desde el DAO`() = runTest {
        val userList = listOf(baseUser)
        coEvery { userDao.observarTodos() } returns flowOf(userList)
        val resultFlow = repository.getAllUsers()
        resultFlow.collect { users ->
            assertEquals(1, users.size)
            assertEquals("  Lucas  ", users[0].nombres)
        }

        // Verify: Confirmamos que la función `observarTodos()` del DAO fue llamada exactamente una vez.
        coVerify(exactly = 1) { userDao.observarTodos() }
    }

    @Test
    fun `getUserById devuelve el usuario correcto cuando existe`() = runTest {
        // Arrange: Simulamos que el DAO devuelve nuestro usuario base cuando se le pide el id 1.
        coEvery { userDao.obtenerPorId(1) } returns baseUser

        // Act: Llamamos a la función del repositorio.
        val result = repository.getUserById(1)

        // Assert: Verificamos que el resultado no es nulo y es el usuario que esperamos.
        assertNotNull(result)
        assertEquals(1, result?.id)
        assertEquals("  lucas.prueba@example.com  ", result?.correo)
        coVerify(exactly = 1) { userDao.obtenerPorId(1) }
    }

    @Test
    fun `getUserById devuelve null cuando el usuario no existe`() = runTest {
        coEvery { userDao.obtenerPorId(99) } returns null
        val result = repository.getUserById(99)
        assertNull(result)
        coVerify(exactly = 1) { userDao.obtenerPorId(99) }
    }

    @Test
    fun `updateUser llama al DAO con los datos limpios (trim)`() = runTest {
        repository.updateUser(baseUser)
        coVerify(exactly = 1) {
            userDao.actualizar(
                match {
                    it.nombres == "Lucas" && it.correo == "lucas.prueba@example.com"
                }
            )
        }
    }

    @Test
    fun `deleteUser llama al metodo eliminar del DAO`() = runTest {
        val userToDelete = baseUser.copy(id = 5)
        repository.deleteUser(userToDelete)
        coVerify(exactly = 1) { userDao.eliminar(userToDelete) }
    }

    @Test
    fun `deleteAllUsers llama al metodo eliminarTodos del DAO`() = runTest {
        coEvery { userDao.eliminarTodos() } just runs
        repository.deleteAllUsers()
        coVerify(exactly = 1) { userDao.eliminarTodos() }
    }
}
