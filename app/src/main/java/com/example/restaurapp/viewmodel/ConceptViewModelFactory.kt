package com.example.restaurapp.viewmodel

import android.content.Context // <-- IMPORTAR
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restaurapp.model.local.AppDatabase // <-- IMPORTAR
import com.example.restaurapp.model.repository.ConceptRepository
import java.lang.IllegalArgumentException

class ConceptViewModelFactory(
    private val repository: ConceptRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConceptViewModel::class.java)) {
            return ConceptViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        fun getInstance(): ConceptViewModelFactory {
            // Ya no necesita DAOs, solo crea el repositorio
            val repository = ConceptRepository()
            return ConceptViewModelFactory(repository)
        }
    }
}