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
data class ConceptUiState(
    val nombreConcepto: String = "",
    val descripcion: String = "",
    val tipoSeleccionado: String = ConceptType.FORMATIVO,

    val families: List<FamiliaNetworkDTO> = emptyList(),
    val conceptosFormativos: List<ConceptoFormativoNetworkDTO> = emptyList(),

    // Campos para Familia
    val familyName: String = "",
    val familyDescription: String = "",
    val currentFamilyId: Long? = null,

    // --- NUEVO: Campos para Subfamilia (Requerido por Backend) ---
    val subfamilyName: String = "",
    val subfamilyDescription: String = "",
    val subfamilies: List<SubfamiliaNetworkDTO> = emptyList(),
    val currentSubfamilyId: Long? = null,

    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,

    val selectedConcept: ConceptoTecnicoNetworkDTO? = null
)

class ConceptViewModel(private val conceptRepository: ConceptRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ConceptUiState())
    val uiState: StateFlow<ConceptUiState> = _uiState.asStateFlow()

    init {
        // Carga inicial para invitado (userId = 0)
        refreshAllData(0)
    }

    /**
     * Carga/refresca todos los datos de familias y conceptos formativos.
     */
    fun refreshAllData(userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val familiasDeferred = async { conceptRepository.getAllFamilies(userId) }
                val formativosDeferred = async { conceptRepository.getConceptosFormativos(userId) }

                val nuevasFamilias = familiasDeferred.await()
                val nuevosFormativos = formativosDeferred.await()

                _uiState.update { currentState ->
                    currentState.copy(
                        families = nuevasFamilias,
                        conceptosFormativos = nuevosFormativos,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al cargar datos: ${e.message}", isLoading = false) }
            }
        }
    }

    /**
     * --- NUEVO: Carga las subfamilias de una familia específica ---
     */
    fun loadSubfamilies(familiaId: Long) {
        _uiState.update { it.copy(isLoading = true, currentFamilyId = familiaId) }
        viewModelScope.launch {
            try {
                // Asegúrate de implementar getSubfamilias en tu Repository
                val subs = conceptRepository.getSubfamilias(familiaId)
                _uiState.update { it.copy(subfamilies = subs, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al cargar subfamilias: ${e.message}", isLoading = false) }
            }
        }
    }

    fun selectConceptById(conceptId: Long) {
        val state = _uiState.value
        var foundConcept: ConceptoTecnicoNetworkDTO? = null

        // 1. Buscar en formativos
        foundConcept = state.conceptosFormativos.find { it.formativeCId == conceptId }?.let {
            ConceptoTecnicoNetworkDTO(
                technicalId = it.formativeCId,
                technicalName = it.formativeName,
                technicalDescription = it.formativeDescription,
                isFavorite = it.isFavorite,
                imageUrl = it.imageUrl
            )
        }

        // 2. Si no, buscar en técnicos (dentro de familias -> conceptos técnicos)
        // Nota: Si ahora tienes subfamilias, la estructura de 'families' podría haber cambiado
        // o necesitarás buscar dentro de 'subfamilies' si ya las tienes cargadas.
        // Asumiendo que el DTO de Familia aún trae una lista plana o que buscas en la lista general:
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

    fun toggleFavorite(userId: Int, conceptId: Long, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch {
            try {
                conceptRepository.toggleFavorite(userId, conceptId, isCurrentlyFavorite)

                // Actualización optimista de la UI
                _uiState.update { currentState ->
                    val newFormativos = currentState.conceptosFormativos.map {
                        if (it.formativeCId == conceptId) it.copy(isFavorite = !isCurrentlyFavorite) else it
                    }
                    // Actualizar también en técnicos si es necesario
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

    // --- FUNCIONES DE CREACIÓN ---

    fun addFamily(currentUserId: Int) = viewModelScope.launch {
        val state = _uiState.value
        if (state.familyName.isBlank() || state.familyDescription.isBlank()) {
            _uiState.update { it.copy(error = "Nombre y descripción son requeridos.") }
            return@launch
        }
        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            // CORREGIDO: Ya no se envía 'familyComponents'
            conceptRepository.addFamily(state.familyName, state.familyDescription, currentUserId)

            _uiState.update { it.copy(
                successMessage = "Familia creada",
                familyName = "",
                familyDescription = ""
            )}
            refreshAllData(currentUserId)
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = e.message) }
        }
    }

    /**
     * --- NUEVO: Crear Subfamilia ---
     */
    fun addSubfamily(currentUserId: Long) = viewModelScope.launch {
        val state = _uiState.value
        val famId = state.currentFamilyId

        if (famId == null) {
            _uiState.update { it.copy(error = "No hay familia seleccionada.") }
            return@launch
        }
        if (state.subfamilyName.isBlank() || state.subfamilyDescription.isBlank()) {
            _uiState.update { it.copy(error = "Completa todos los campos de la subfamilia.") }
            return@launch
        }

        _uiState.update { it.copy(isLoading = true, error = null) }
        try {
            // Asegúrate de implementar addSubfamily en tu Repository
            conceptRepository.addSubfamily(
                state.subfamilyName,
                state.subfamilyDescription,
                currentUserId,
                famId
            )
            _uiState.update { it.copy(
                successMessage = "Subfamilia creada",
                subfamilyName = "",
                subfamilyDescription = ""
            )}
            // Recargar subfamilias para ver la nueva
            loadSubfamilies(famId)
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
                // CORREGIDO: Ahora validamos subfamilia, no familia
                if (state.currentSubfamilyId == null) {
                    _uiState.update { it.copy(isLoading = false, error = "Debes seleccionar una subfamilia.") }
                    return@launch
                }

                // CORREGIDO: Pasamos el ID de la Subfamilia
                conceptRepository.addConceptoTecnico(
                    state.nombreConcepto,
                    state.descripcion,
                    currentUserId,
                    state.currentSubfamilyId // Usamos el ID de subfamilia
                )
            }

            _uiState.update { it.copy(
                successMessage = "Concepto creado",
                nombreConcepto = "",
                descripcion = ""
            )}

            // Refrescar datos según corresponda
            if (state.tipoSeleccionado == ConceptType.FORMATIVO) {
                refreshAllData(currentUserId.toInt())
            } else {
                // Si es técnico, recargar la familia actual o subfamilias
                state.currentFamilyId?.let { loadSubfamilies(it) }
            }

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

    // Setters para Subfamilia
    fun onSubfamilyNameChange(name: String) { _uiState.update { it.copy(subfamilyName = name, error = null) } }
    fun onSubfamilyDescriptionChange(desc: String) { _uiState.update { it.copy(subfamilyDescription = desc, error = null) } }

    fun setCurrentFamilyId(id: Long?) { _uiState.update { it.copy(currentFamilyId = id) } }
    fun setCurrentSubfamilyId(id: Long?) { _uiState.update { it.copy(currentSubfamilyId = id) } }

    fun clearMessages() { _uiState.update { it.copy(error = null, successMessage = null) } }
}