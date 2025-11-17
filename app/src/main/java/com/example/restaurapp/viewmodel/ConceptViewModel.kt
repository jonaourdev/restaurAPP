package com.example.restaurapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurapp.model.local.concepts.ConceptEntity
import com.example.restaurapp.model.local.concepts.ConceptType
import com.example.restaurapp.model.local.concepts.ConceptWithFavorite // Importa el nuevo modelo
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
    // Datos principales: la lista ahora contiene el estado de favorito
    val concepts: List<ConceptWithFavorite> = emptyList(),
    // Concepto seleccionado actualmente (para ver detalles o editar)
    val selectedConcept: ConceptWithFavorite? = null
)

class ConceptViewModel(private val conceptRepository: ConceptRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ConceptUiState())
    val uiState: StateFlow<ConceptUiState> = _uiState.asStateFlow()

    init {
        // --- CARGA PÚBLICA INICIAL ---
        // Al iniciar el ViewModel, cargamos todos los datos que son públicos
        // y visibles para cualquier usuario, incluido el invitado.
        fetchAllPublicData()
        fetchAllFamilies()
    }

    /**
     * Carga los datos de conceptos que no dependen de un usuario.
     * Usa un `userId` ficticio (ej. 0) para obtener la lista sin favoritos.
     */
    private fun fetchAllPublicData() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            // Usamos un userId que no existirá (como 0) para asegurar que ningún concepto
            // venga marcado como favorito por defecto.
            conceptRepository.getAllConcepts(userId = 0).collect { publicConcepts ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        concepts = publicConcepts
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = "Error al cargar datos públicos: ${e.message}") }
        }
    }

    /**
     * Actualiza la lista de conceptos con el estado de favorito para un usuario específico.
     * Esta función es llamada desde la UI cuando un usuario inicia sesión.
     */
    fun updateUserFavorites(userId: Int) {
        viewModelScope.launch {
            // No mostramos el indicador de carga para que la actualización sea fluida.
            try {
                conceptRepository.getAllConcepts(userId).collect { conceptsWithFavorites ->
                    _uiState.update { it.copy(concepts = conceptsWithFavorites) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al actualizar favoritos: ${e.message}") }
            }
        }
    }

    /**
     * Limpia solo los datos de favoritos del usuario al cerrar sesión,
     * volviendo a cargar la lista de conceptos pública.
     */
    fun clearUserFavorites() {
        // Simplemente volvemos a cargar la lista pública, que no tiene favoritos.
        fetchAllPublicData()
    }

    fun toggleFavorite(conceptWithFavorite: ConceptWithFavorite, userId: Int) = viewModelScope.launch {
        try {
            val conceptId = conceptWithFavorite.concept.id
            if (conceptWithFavorite.isFavorite) {
                conceptRepository.removeFavorite(userId, conceptId)
            } else {
                conceptRepository.addFavorite(userId, conceptId)
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = "Error al actualizar el favorito: ${e.message}") }
        }
    }

    // --- El resto de las funciones permanecen mayormente igual ---

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

    fun selectConceptById(conceptId: Long) {
        val concept = _uiState.value.concepts.find { it.concept.id == conceptId }
        _uiState.update { it.copy(selectedConcept = concept) }
    }

    fun clearMessages() {
        _uiState.update { it.copy(error = null, successMessage = null) }
    }

    private fun fetchAllFamilies() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        try {
            conceptRepository.getAllFamilies().collect { familyList ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        families = familyList
                    )
                }
            }
        } catch (e: Exception){
            _uiState.update { it.copy(isLoading = false, error = "Error al cargar las familias: ${e.message}") }
        }
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

    // Esta función ya no es necesaria con la nueva lógica, pero la dejamos por si la usas en otro lado.
    // La nueva función es `clearUserFavorites`.
    fun clearUserSpecificData() {
        _uiState.update {
            ConceptUiState()
        }
    }
}
