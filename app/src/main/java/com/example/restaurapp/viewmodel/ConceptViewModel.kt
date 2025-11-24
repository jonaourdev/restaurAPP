package com.example.restaurapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurapp.model.local.concepts.ConceptType
import com.example.restaurapp.model.network.*
import com.example.restaurapp.model.repository.ConceptRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// --- UiState ACTUALIZADO ---
// Mantiene los datos de la red
data class ConceptUiState(
    val nombreConcepto: String = "",
    val descripcion: String = "",
    val tipoSeleccionado: String = ConceptType.FORMATIVO,

    val families: List<FamiliaNetworkDTO> = emptyList(),
    val conceptosFormativos: List<ConceptoFormativoNetworkDTO> = emptyList(),

    val familyName: String = "",
    val familyDescription: String = "",
    val familyComponents: String = "", // 🚩 CAMBIO 1: Estado para el nuevo campo
    val currentFamilyId: Long? = null,

    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,

    val selectedConcept: ConceptoTecnicoNetworkDTO? = null

    // Estado para la pantalla de detalle (opcional pero recomendado)
    // val selectedConcept: ConceptoTecnicoNetworkDTO? = null,
    // val selectedFormativo: ConceptoFormativoNetworkDTO? = null
)

class ConceptViewModel(private val conceptRepository: ConceptRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ConceptUiState())
    val uiState: StateFlow<ConceptUiState> = _uiState.asStateFlow()

    init {
        // Carga inicial para invitado (userId = 0)
        refreshAllData(0)
    }

    /**
     * Carga/refresca todos los datos de familias y conceptos
     * para un usuario específico (0 si es invitado).
     */
    fun refreshAllData(userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // 1. Inicia ambas llamadas en paralelo con 'async'
                val familiasDeferred = async { conceptRepository.getAllFamilies(userId) }
                val formativosDeferred = async { conceptRepository.getConceptosFormativos(userId) }

                // 2. Espera a que ambas terminen y recoge los resultados
                val nuevasFamilias = familiasDeferred.await()
                val nuevosFormativos = formativosDeferred.await()

                // 3. Realiza UNA SOLA actualización del estado con TODOS los datos nuevos
                _uiState.update { currentState ->
                    currentState.copy(
                        families = nuevasFamilias,
                        conceptosFormativos = nuevosFormativos,
                        isLoading = false // Apagamos el loading aquí
                    )
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al cargar datos: ${e.message}", isLoading = false) }
            }
            // El 'finally' ya no es necesario porque controlamos 'isLoading' en los bloques try/catch
        }
    }

    fun selectConceptById(conceptId: Long) {
        val state = _uiState.value
        var foundConcept: ConceptoTecnicoNetworkDTO? = null

        // Primero busca en los conceptos formativos
        foundConcept = state.conceptosFormativos.find { it.formativeCId == conceptId }?.let {
            // Mapea el formativo a un DTO técnico genérico para la pantalla de detalle
            ConceptoTecnicoNetworkDTO(
                technicalId = it.formativeCId,
                technicalName = it.formativeName,
                technicalDescription = it.formativeDescription,
                isFavorite = it.isFavorite,
                imageUrl = it.imageUrl
            )
        }

        // Si no lo encontró, busca en los conceptos técnicos dentro de cada familia
        if (foundConcept == null) {
            for (family in state.families) {
                val concept = family.conceptosTecnicos.find { it.technicalId == conceptId }
                if (concept != null) {
                    foundConcept = concept
                    break
                }
            }
        }

        _uiState.update { it.copy(selectedConcept = foundConcept) }
    }


    fun updateUserFavorites(userId: Int) {
        refreshAllData(userId)
    }

    fun clearUserFavorites() {
        _uiState.update { currentState ->
            val formativosLimpios = currentState.conceptosFormativos.map { it.copy(isFavorite = false) }

            val familiasLimpias = currentState.families.map { family ->
                family.copy(
                    conceptosTecnicos = family.conceptosTecnicos.map { it.copy(isFavorite = false) }
                )
            }

            currentState.copy(
                conceptosFormativos = formativosLimpios,
                families = familiasLimpias
            )
        }
    }

    fun toggleFavorite(userId: Int, conceptId: Long, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch {
            try {
                conceptRepository.toggleFavorite(userId, conceptId, isCurrentlyFavorite)

                _uiState.update { currentState ->
                    val newFormativos = currentState.conceptosFormativos.map {
                        if (it.formativeCId == conceptId) it.copy(isFavorite = !isCurrentlyFavorite) else it
                    }
                    val newFamilies = currentState.families.map { family ->
                        family.copy(conceptosTecnicos = family.conceptosTecnicos.map {
                            if (it.technicalId == conceptId) it.copy(isFavorite = !isCurrentlyFavorite) else it
                        })
                    }
                    currentState.copy(
                        conceptosFormativos = newFormativos,
                        families = newFamilies
                    )
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al guardar favorito: ${e.message}") }
            }
        }
    }

    fun addFamily(currentUserId: Int) = viewModelScope.launch {
        val state = _uiState.value
        // Validación mejorada para incluir el nuevo campo
        if (state.familyName.isBlank() || state.familyComponents.isBlank()) {
            _uiState.update { it.copy(error = "El nombre y los componentes no pueden estar vacíos.") }
            return@launch
        }
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            // 🚩 CAMBIO 3: Pasar el nuevo campo familyComponents al repositorio (requiere cambiar la firma del repositorio)
            conceptRepository.addFamily(state.familyName, state.familyDescription, state.familyComponents, currentUserId)
            _uiState.update { it.copy(
                successMessage = "Familia creada",
                familyName = "",
                familyDescription = "",
                familyComponents = "" // 🚩 Limpiar campo
            )}
            refreshAllData(currentUserId)
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = e.message) }
        }
    }

    fun addConcept(currentUserId: Long) = viewModelScope.launch {
        val state = _uiState.value
        if (state.nombreConcepto.isBlank()) {
            _uiState.update { it.copy(error = "El nombre no puede estar vacío.") }
            return@launch
        }
        _uiState.update { it.copy(isLoading = true, error = null) }

        try {
            if (state.tipoSeleccionado == ConceptType.FORMATIVO) {
                conceptRepository.addConceptoFormativo(
                    state.nombreConcepto,
                    state.descripcion,
                    currentUserId
                )
            } else { // Es TECNICO
                if (state.currentFamilyId == null) {
                    _uiState.update { it.copy(isLoading = false, error = "No se seleccionó una familia.") }
                    return@launch
                }
                conceptRepository.addConceptoTecnico(
                    state.nombreConcepto,
                    state.descripcion,
                    currentUserId,
                    state.currentFamilyId
                )
            }

            _uiState.update { it.copy(
                successMessage = "Concepto creado",
                nombreConcepto = "",
                descripcion = ""
            )}
            refreshAllData(currentUserId.toInt())

        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = e.message) }
        }
    }

    // --- Setters ---
    fun onConceptNameChange(name: String) { _uiState.update { it.copy(nombreConcepto = name, error = null) } }
    fun onDescriptionChange(description: String) { _uiState.update { it.copy(descripcion = description, error = null) } }
    fun onConceptTypeChange(tipo: String){ _uiState.update { it.copy(tipoSeleccionado = tipo) } }
    fun onFamilyNameChange(name: String) { _uiState.update { it.copy(familyName = name, error = null) } }
    fun onFamilyDescriptionChange(description: String) { _uiState.update { it.copy(familyDescription = description, error = null) } }

    // 🚩 CAMBIO 2: Nuevo setter para el campo familyComponents
    fun onFamilyComponentsChange(components: String) { _uiState.update { it.copy(familyComponents = components, error = null) } }

    fun setCurrentFamilyId(id: Long?) { _uiState.update { it.copy(currentFamilyId = id) } }
    fun clearMessages() { _uiState.update { it.copy(error = null, successMessage = null) } }
}