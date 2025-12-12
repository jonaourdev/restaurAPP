package com.example.restaurapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restaurapp.model.network.RetrofitClient
import com.example.restaurapp.model.repository.ConceptRepository
import java.lang.IllegalArgumentException

class ConceptViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConceptViewModel::class.java)) {
            val apiService = RetrofitClient.apiService
            val repository = ConceptRepository(apiService)
            return ConceptViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        fun getInstance(): ConceptViewModelFactory {
            return ConceptViewModelFactory()
        }
    }
}