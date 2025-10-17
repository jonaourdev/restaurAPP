package com.example.restaurapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.restaurapp.model.local.AppDatabase
import com.example.restaurapp.model.repository.RegisterRepository

class RegisterViewModelFactory (private val app: Application): ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.get(app)
        val repository = RegisterRepository(db.registerDao())
        return RegisterViewModel(repository) as T
    }
}