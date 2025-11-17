package com.example.restaurapp.model.repository

import com.example.restaurapp.model.local.user.UserDao
import com.example.restaurapp.model.local.user.UserEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class AuthRepositoryTest {

    // 1. DAO simulado (mock) con MockK.
    private val userDao: UserDao = mockk(relaxed = true)

    // 2. El Repositorio a probar.
    private lateinit var repository: AuthRepository

    // 3. Un usuario base para reutilizar.
    private val baseUser = UserEntity(
        id = 1,
        nombres = "Lucas",
        apellidos = "Tester",
        correo = "lucas.test@example.com",
        contrasenna = "password123"
    )

    // Se ejecuta antes de cada test para un estado limpio.
    @BeforeEach
    fun setUp() {
        repository = AuthRepository(userDao)
    }

    // --- Tests para la función `register` ---

    @Test
    fun `register DEBE insertar un nuevo usuario CUANDO el correo no existe`() = runTest {
        // Arrange: Simulamos que el correo NO está en uso.
        coEvery { userDao.contarPorCorreo("nuevo.usuario@example.com") } returns 0

        // Act: Ejecutamos el registro.
        repository.register(
            nombres = "  Nuevo  ",
            apellidos = "Usuario",
            correo = "  nuevo.usuario@example.com  ",
            contrasenna = "claveSegura"
        )

        // Verify: Confirmamos que se llamó a 'insertar' en el DAO con los datos limpios (trim).
        coVerify(exactly = 1) {
            userDao.insertar(
                match {
                    it.nombres == "Nuevo" && it.correo == "nuevo.usuario@example.com"
                }
            )
        }
    }

    @Test
    fun `register DEBE lanzar una excepción CUANDO el correo ya existe`() = runTest {
        // Arrange: Simulamos que el correo SÍ está en uso (el DAO devuelve un contador > 0).
        coEvery { userDao.contarPorCorreo("lucas.test@example.com") } returns 1

        // Act & Assert: Verificamos que al intentar registrar con el mismo correo, se lanza una excepción.
        val exception = assertThrows<Exception> {
            repository.register(
                nombres = "Otro",
                apellidos = "Usuario",
                correo = "lucas.test@example.com",
                contrasenna = "otraClave"
            )
        }

        // Verificamos que el mensaje de la excepción es el que esperamos.
        assertEquals("El correo electrónico ya está en uso.", exception.message)

        // Verify: Confirmamos que, debido a la excepción, el método 'insertar' del DAO NUNCA fue llamado.
        coVerify(exactly = 0) { userDao.insertar(any()) }
    }


    // --- Tests para la función `login` ---

    @Test
    fun `login DEBE devolver el UserEntity CUANDO el correo y la contraseña son correctos`() = runTest {
        // Arrange: Simulamos que el DAO encuentra un usuario con el correo y la contraseña correctos.
        coEvery { userDao.obtenerPorCorreo("lucas.test@example.com") } returns baseUser

        // Act: Ejecutamos el login.
        val result = repository.login("  lucas.test@example.com  ", "password123")

        // Assert: Verificamos que el resultado no es nulo y es el usuario que esperamos.
        assertNotNull(result)
        assertEquals(baseUser.id, result?.id)
        assertEquals("Lucas", result?.nombres)

        // Verify: Confirmamos que se llamó a 'obtenerPorCorreo' con el correo limpio.
        coVerify(exactly = 1) { userDao.obtenerPorCorreo("lucas.test@example.com") }
    }

    @Test
    fun `login DEBE devolver null CUANDO la contraseña es incorrecta`() = runTest {
        // Arrange: Simulamos que el DAO encuentra un usuario, pero la contraseña que pasamos es incorrecta.
        coEvery { userDao.obtenerPorCorreo("lucas.test@example.com") } returns baseUser

        // Act: Ejecutamos el login con la contraseña equivocada.
        val result = repository.login("lucas.test@example.com", "password_incorrecta")

        // Assert: Verificamos que el resultado es nulo.
        assertNull(result)
    }

    @Test
    fun `login DEBE devolver null CUANDO el correo no existe`() = runTest {
        // Arrange: Simulamos que el DAO no encuentra ningún usuario con ese correo.
        coEvery { userDao.obtenerPorCorreo("noexiste@example.com") } returns null

        // Act: Ejecutamos el login con un correo que no está en la base de datos.
        val result = repository.login("noexiste@example.com", "cualquier_password")

        // Assert: Verificamos que el resultado es nulo.
        assertNull(result)
    }
}
