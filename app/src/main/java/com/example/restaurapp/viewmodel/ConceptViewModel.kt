package com.example.restaurapp.viewmodel

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurapp.model.local.concepts.ConceptEntity
import com.example.restaurapp.model.repository.ConceptRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ConceptUiState (
    val nombreConcepto: String,
    val descripcion: String
)

class ConceptViewModel(private val conceptRepository: ConceptRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ConceptUiState())
    val uiState: StateFlow<ConceptUiState> = _uiState.asStateFlow()

    fun addConcept() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.nombreConcepto.isNotBlank() && currentState.descripcion.isNotBlank()) {
                val newConcept = ConceptEntity(
                    name = currentState.nombreConcepto,
                    description = currentState.descripcion
                )
                conceptRepository.insert(newConcept)
            }
        }
    }

}


