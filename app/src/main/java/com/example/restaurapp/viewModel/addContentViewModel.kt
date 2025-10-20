package com.example.restaurapp.viewmodelimport

import androidx.compose.runtime.getValue


import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurapp.model.local.user.FamilyEntity
import com.example.restaurapp.model.local.user.FormativeConceptEntity
import com.example.restaurapp.model.local.user.TechnicalConceptEntity
import com.example.restaurapp.model.repository.ContentRepository // <<< 1. IMPORTA EL REPOSITORIO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de "Añadir Contenido".
 * Ahora utiliza ContentRepository para abstraer el origen de los datos.
 */
// <<< 2. RECIBE EL REPOSITORIO, NO LOS DAOs
class AddContentViewModel(private val repository: ContentRepository) : ViewModel() {

    // --- ESTADO DE LA UI (Sin cambios) ---

    var newFamilyName by mutableStateOf("")
    var newFamilyDescription by mutableStateOf("")
    var newConceptName by mutableStateOf("")
    var newConceptDescription by mutableStateOf("")

    // <<< 3. USA EL REPOSITORIO PARA OBTENER LOS DATOS
    val families: StateFlow<List<FamilyEntity>> = repository.allFamilies
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- EVENTOS DE LA UI (Sin cambios en las funciones 'on...Change') ---

    fun onFamilyNameChange(name: String) { newFamilyName = name }
    fun onFamilyDescriptionChange(description: String) { newFamilyDescription = description }
    fun onConceptNameChange(name: String) { newConceptName = name }
    fun onConceptDescriptionChange(description: String) { newConceptDescription = description }

    // --- LÓGICA DE NEGOCIO ---

    fun saveNewFamily() {
        if (newFamilyName.isBlank()) return

        viewModelScope.launch {
            val newFamily = FamilyEntity(
                name = newFamilyName,
                description = newFamilyDescription
            )
            // <<< 4. USA EL REPOSITORIO PARA GUARDAR
            repository.insertFamily(newFamily)
            // Limpiar formulario después de guardar
            newFamilyName = ""
            newFamilyDescription = ""
        }
    }

    fun saveNewFormativeConcept() {
        if (newConceptName.isBlank()) return

        viewModelScope.launch {
            val newConcept = FormativeConceptEntity(
                name = newConceptName,
                description = newConceptDescription
            )
            // <<< 4. USA EL REPOSITORIO PARA GUARDAR
            repository.insertFormativeConcept(newConcept)
            newConceptName = ""
            newConceptDescription = ""
        }
    }

    fun saveNewTechnicalConcept(familyId: Int) {
        if (newConceptName.isBlank()) return

        viewModelScope.launch {
            val newConcept = TechnicalConceptEntity(
                name = newConceptName,
                description = newConceptDescription,
                familyId = familyId
            )
            // <<< 4. USA EL REPOSITORIO PARA GUARDAR
            repository.insertTechnicalConcept(newConcept)
            newConceptName = ""
            newConceptDescription = ""
        }
    }
}
