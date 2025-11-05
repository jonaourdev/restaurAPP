package com.example.restaurapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurapp.model.local.concepts.ConceptEntity
import com.example.restaurapp.model.local.concepts.ConceptType
import com.example.restaurapp.model.repository.ConceptRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConceptUiState(
    // Campos para el formulario de creación/edición de conceptos
    val nombreConcepto: String = "",
    val descripcion: String = "",
    val tipoSeleccionado: String = ConceptType.FORMATIVO,

    // Campo para la funcionalidad de búsqueda
    val searchQuery: String = "",

    // Estado global de la pantalla de conceptos
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,

    // Datos principales: la lista de conceptos a mostrar
    val concepts: List<ConceptEntity> = emptyList(),

    // Concepto seleccionado actualmente (para ver detalles o editar)
    val selectedConcept: ConceptEntity? = null
)

class ConceptViewModel(private val conceptRepository: ConceptRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ConceptUiState())
    val uiState: StateFlow<ConceptUiState> = _uiState.asStateFlow()

    init {
        fetchAllConcepts()
    }

    fun onConceptNameChange(name: String) {
        _uiState.update { it.copy(nombreConcepto = name, error = null) }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(descripcion = description, error = null) }
    }

    fun onConceptTypeChange(tipo: String){
        _uiState.update { it.copy(tipoSeleccionado = tipo) }
    }


    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }


    // --- OPERACIONES CRUD (Crear, Leer, Actualizar, Borrar) ---
    private fun fetchAllConcepts() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            conceptRepository.getAllConcepts().collect { conceptList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        concepts = conceptList
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = "Error al cargar los conceptos: ${e.message}") }
        }
    }

    fun addConcept() = viewModelScope.launch {
        val state = _uiState.value

        if (state.nombreConcepto.isBlank() || state.descripcion.isBlank()) {
            _uiState.update { it.copy(error = "El nombre y la descripción no pueden estar vacíos.") }
            return@launch
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        try {
            val newConcept = ConceptEntity(
                nombreConcepto = state.nombreConcepto.trim(),
                descripcion = state.descripcion.trim(),
                tipo = state.tipoSeleccionado
            )
            conceptRepository.insert(newConcept)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    successMessage = "Concepto '${newConcept.nombreConcepto}' añadido con éxito.",
                    nombreConcepto = "",
                    descripcion = ""
                )
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = "Error al guardar el concepto: ${e.message}") }
        }
    }

    fun selectConcept(concept: ConceptEntity) {
        _uiState.update { it.copy(selectedConcept = concept) }
    }


    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }
}


