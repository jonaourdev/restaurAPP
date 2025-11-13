package com.example.restaurapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurapp.model.local.concepts.ConceptEntity
import com.example.restaurapp.model.local.concepts.ConceptType
import com.example.restaurapp.model.local.concepts.FamilyEntity
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

    //Campos para familias
    val families: List<FamilyEntity> = emptyList(),
    val familyName: String = "",
    val familyDescription: String = "",
    val currentFamilyId: Long? = null,


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
        fetchAllFamilies()
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


    // --- OPERACIONES CRUD CONCEPTOS (Crear, Leer, Actualizar, Borrar) ---
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
                tipo = state.tipoSeleccionado,
                familyId = state.currentFamilyId
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

    fun toggleFavorite(concept: ConceptEntity) = viewModelScope.launch {
        val updatedConcept = concept.copy(isFavorite = !concept.isFavorite)
        try {
            conceptRepository.update(updatedConcept)
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Error al actualizar el favorito: ${e.message}") }
        }
    }

    fun selectConcept(concept: ConceptEntity) {
        _uiState.update { it.copy(selectedConcept = concept) }
    }

    //Seleccionar Concepto por ID para desplegar el detalle
    fun selectConceptById(conceptId: Long) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            conceptRepository.getAllConcepts().collect { latestConcepts ->
                val concept = latestConcepts.find { it.id == conceptId }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        concepts = latestConcepts,
                        selectedConcept = concept
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = "No se pudo cargar el concepto: ${e.message}") }
        }
    }


    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }

    // --- OPERACIONES CRUD FAMILIAS (Crear, Leer, Actualizar, Borrar) ---

    //Obtener todas las familias
    private fun fetchAllFamilies() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            conceptRepository.getAllFamilies().collect { familyList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        families = familyList
                    ) }
        }
    } catch (e: Exception){
        _uiState.update { it.copy(isLoading = false, error = "Error al cargar las familias: ${e.message}") }}
    }

    fun onFamilyNameChange(name: String) {
        _uiState.update { it.copy(familyName = name, error = null) }
    }

    fun onFamilyDescriptionChange(description: String) {
        _uiState.update { it.copy(familyDescription = description, error = null) }
    }

    fun addFamily() = viewModelScope.launch {
        val state = _uiState.value
        if (state.familyName.isBlank()){
            _uiState.update { it.copy(error = "El nombre de la familia no puede estar vacío.") }
            return@launch
        }
        val newFamily = FamilyEntity(name = state.familyName, description = state.familyDescription)
        conceptRepository.insertFamily(newFamily)
        _uiState.update { it.copy(familyName = "", familyDescription = "") }
    }

    fun setCurrentFamilyId(id: Long?) {
        _uiState.update { it.copy(currentFamilyId = id) }
    }


}
