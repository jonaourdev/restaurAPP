package com.example.restaurapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
// CORRECCIÓN: Se separaron las dos importaciones en líneas distintas
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Regla de JUnit5 que reemplaza el Dispatcher.Main por un TestDispatcher
 * para probar ViewModels y corrutinas de manera controlada.
 */
@ExperimentalCoroutinesApi
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : BeforeEachCallback, AfterEachCallback {

    // Se ejecuta ANTES de cada test.
    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(testDispatcher)
    }

    // Se ejecuta DESPUÉS de cada test.
    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}
