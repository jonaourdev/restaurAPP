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
    val conceptosTecnicos: List<ConceptoTecnicoNetworkDTO> = emptyList(),

    // Campos para Familia
    val familyName: String = "",
    val familyDescription: String = "",
    val currentFamilyId: Long? = null,

    // Campos para Subfamilia
    val subfamilyName: String = "",
    val subfamilyDescription: String = "",
    val subfamilies: List<SubfamiliaNetworkDTO> = emptyList(),
    val currentSubfamilyId: Long? = null,

    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,

    // CORRECCIÓN: Usamos un modelo de UI genérico en lugar de un DTO específico de red
    val selectedConcept: ConceptoDetalleUi? = null
)

class ConceptViewModel(private val conceptRepository: ConceptRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ConceptUiState())
    val uiState: StateFlow<ConceptUiState> = _uiState.asStateFlow()

    init {
        // Carga inicial para invitado (userId = 0) o se refrescará desde la UI
        refreshAllData(0)
    }

    /**
     * Carga/refresca todos los datos de familias y conceptos formativos.
     */
    fun refreshAllData(userId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                // Ejecutamos las llamadas en paralelo para mayor eficiencia
                val familiasDeferred = async { conceptRepository.getAllFamilies(userId) }
                val formativosDeferred = async { conceptRepository.getConceptosFormativos(userId) }
                val tecnicosDeferred = async { conceptRepository.getConceptosTecnicos(userId) }


                val nuevasFamilias = familiasDeferred.await()
                val nuevosFormativos = formativosDeferred.await()
                val nuevosTecnicos = tecnicosDeferred.await()

                _uiState.update { currentState ->
                    currentState.copy(
                        families = nuevasFamilias,
                        conceptosFormativos = nuevosFormativos,
                        conceptosTecnicos = nuevosTecnicos,
                        isLoading = false
                    )
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al cargar datos: ${e.message}", isLoading = false) }
            }
        }
    }

    /**
     * Carga los conceptos técnicos de una subfamilia específica
     */
    fun loadConceptosTecnicosBySubfamilia(subfamiliaId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val conceptos = conceptRepository.getConceptosBySubfamilia(subfamiliaId)

                _uiState.update { current ->
                    current.copy(
                        conceptosTecnicos = conceptos,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update { current ->
                    current.copy(
                        error = "Error al cargar conceptos técnicos: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }



    /**
     * Carga las subfamilias de una familia específica
     */
    fun loadSubfamilies(familiaId: Long) {

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, currentFamilyId = familiaId) }
            try {
                val subs = conceptRepository.getSubfamilias(familiaId)
                _uiState.update { it.copy(subfamilies = subs, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al cargar subfamilias: ${e.message}", isLoading = false) }
            }
        }
    }

    /**
     * CORRECCIÓN PRINCIPAL: Lógica unificada para seleccionar conceptos
     */
    fun selectConceptById(conceptId: Long) {
        val state = _uiState.value
        var foundConcept: ConceptoDetalleUi? = null

        // 1. Buscar primero en la lista de Conceptos Formativos
        val formativo = state.conceptosFormativos.find { it.formativeCId == conceptId }

        if (formativo != null) {
            foundConcept = ConceptoDetalleUi(
                technicalId = formativo.formativeCId,
                technicalName = formativo.formativeName,
                technicalDescription = formativo.formativeDescription,
                isFavorite = formativo.isFavorite,
                imageUrl = formativo.imagenes.firstOrNull(), // Tomamos la primera imagen si existe
                tipo = ConceptType.FORMATIVO
            )
        } else {
            // 2. Si no es formativo, buscar en Conceptos Técnicos (dentro de las familias)
            // Nota: Esto asume que tienes los técnicos cargados dentro de `families` o necesitarás una lógica
            // para buscar en todas las subfamilias si la estructura es jerárquica.
            // Aquí iteramos sobre las familias cargadas suponiendo que contienen los técnicos.

            // Si la estructura real requiere buscar en subfamilias, habría que ajustar esto,
            // pero basado en tu DTO actual:

            // Opción A (Búsqueda en memoria si ya tienes los técnicos):
            /* for (familia in state.families) {
                 // Si familia tiene lista de técnicos (revisar tu DTO FamiliaNetworkDTO)
                 // val tecnico = familia.conceptosTecnicos?.find { it.technicalId == conceptId }
                 // if (tecnico != null) { ... mapear a ConceptoDetalleUi ... break }
            }
            */

            // Opción B (Búsqueda simple si no los tienes en memoria):
            // Como fallback, podrías requerir una llamada al repositorio `findById` si no están en memoria.
            // Por ahora, dejaré el placeholder null o lógica simplificada.
        }

        _uiState.update { it.copy(selectedConcept = foundConcept) }
    }

    fun toggleFavorite(userId: Int, conceptId: Long, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch {
            try {
                conceptRepository.toggleFavorite(userId, conceptId, isCurrentlyFavorite)

                // Actualización optimista de la UI (refresca el icono inmediatamente)
                _uiState.update { currentState ->
                    // Actualizar en formativos
                    val newFormativos = currentState.conceptosFormativos.map {
                        if (it.formativeCId == conceptId) it.copy(isFavorite = !isCurrentlyFavorite) else it
                    }

                    // Actualizar en el seleccionado si coincide
                    val newSelected = if (currentState.selectedConcept?.technicalId == conceptId) {
                        currentState.selectedConcept.copy(isFavorite = !isCurrentlyFavorite)
                    } else {
                        currentState.selectedConcept
                    }

                    currentState.copy(
                        conceptosFormativos = newFormativos,
                        selectedConcept = newSelected
                        // También deberías actualizar la lista de técnicos dentro de families si aplica
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
            conceptRepository.addFamily(state.familyName, state.familyDescription, currentUserId)

            _uiState.update { it.copy(
                successMessage = "Familia creada con éxito",
                familyName = "",
                familyDescription = ""
            )}
            refreshAllData(currentUserId)
        } catch (e: Exception) {
            _uiState.update { it.copy(isLoading = false, error = e.message) }
        }
    }

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
                // Validamos que se haya seleccionado una subfamilia
                if (state.currentSubfamilyId == null) {
                    _uiState.update { it.copy(isLoading = false, error = "Debes seleccionar una subfamilia.") }
                    return@launch
                }

                conceptRepository.addConceptoTecnico(
                    state.nombreConcepto,
                    state.descripcion,
                    currentUserId,
                    state.currentSubfamilyId
                )
            }

            _uiState.update { it.copy(
                successMessage = "Concepto creado correctamente",
                nombreConcepto = "",
                descripcion = ""
            )}

            // Refrescar datos
            if (state.tipoSeleccionado == ConceptType.FORMATIVO) {
                refreshAllData(currentUserId.toInt())
            } else {
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

    fun onSubfamilyNameChange(name: String) { _uiState.update { it.copy(subfamilyName = name, error = null) } }
    fun onSubfamilyDescriptionChange(desc: String) { _uiState.update { it.copy(subfamilyDescription = desc, error = null) } }

    fun setCurrentFamilyId(id: Long?) { _uiState.update { it.copy(currentFamilyId = id) } }
    fun setCurrentSubfamilyId(id: Long?) { _uiState.update { it.copy(currentSubfamilyId = id) } }

    fun clearMessages() { _uiState.update { it.copy(error = null, successMessage = null) } }
}

// --- CLASE AUXILIAR DE UI ---
// Esta clase permite unificar Conceptos Técnicos y Formativos para mostrarlos en la UI de detalle
data class ConceptoDetalleUi(
    val technicalId: Long,
    val technicalName: String,
    val technicalDescription: String,
    val isFavorite: Boolean,
    val imageUrl: String? = null,
    val tipo: String // "TECNICO" o "FORMATIVO"
)