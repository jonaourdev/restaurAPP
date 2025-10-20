// en app/src/main/java/com/example/restaurapp/viewmodel/AddContentViewModelFactory.kt
package com.example.restaurapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restaurapp.model.repository.ContentRepository // <<< 1. IMPORTA EL REPOSITORIO
import com.example.restaurapp.viewmodelimport.AddContentViewModel

/**
 * Fábrica para crear una instancia de AddContentViewModel.
 * Es necesaria porque el ViewModel ahora tiene una dependencia (ContentRepository) en su constructor.
 */
// <<< 2. RECIBE EL REPOSITORIO, NO LOS DAOs
class AddContentViewModelFactory(
    private val repository: ContentRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddContentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // <<< 3. PASA EL REPOSITORIO AL CONSTRUCTOR DEL VIEWMODEL
            return AddContentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
