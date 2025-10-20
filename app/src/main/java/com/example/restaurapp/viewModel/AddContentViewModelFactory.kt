// en app/src/main/java/com/example/restaurapp/viewmodel/AddContentViewModelFactory.kt
package com.example.restaurapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restaurapp.data.local.ConceptDao
import com.example.restaurapp.data.local.FamilyDao

/**
 * Fábrica para crear una instancia de AddContentViewModel.
 * Es necesaria porque el ViewModel tiene dependencias (DAOs) en su constructor.
 */
class AddContentViewModelFactory(
    private val familyDao: FamilyDao,
    private val conceptDao: ConceptDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddContentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddContentViewModel(familyDao, conceptDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
