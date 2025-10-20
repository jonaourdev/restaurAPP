// en app/src/main/java/com/example/restaurapp/viewmodel/AddContentViewModel.kt
package com.example.restaurapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restaurapp.model.local.user.ConceptDao
import com.example.restaurapp.model.local.user.FamilyDao
import com.example.restaurapp.model.local.user.FamilyEntity
import com.example.restaurapp.model.local.user.FormativeConceptEntity
import com.example.restaurapp.model.local.user.TechnicalConceptEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de "Añadir Contenido".
 * Maneja el estado del formulario y la lógica para guardar en la base de datos.
 */
class AddContentViewModel(
    private val familyDao: FamilyDao,
    private val conceptDao: ConceptDao
) : ViewModel() {

    // --- ESTADO DE LA UI ---

    // Estado del formulario para añadir una nueva FAMILIA
    var newFamilyName by mutableStateOf("")
    var newFamilyDescription by mutableStateOf("")

    // Estado del formulario para añadir un nuevo CONCEPTO
    var newConceptName by mutableStateOf("")
    var newConceptDescription by mutableStateOf("")

    // Mantiene la lista de familias existentes para mostrar en el desplegable
    val families: StateFlow<List<FamilyEntity>> = familyDao.getAllFamilies()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- EVENTOS DE LA UI ---

    fun onFamilyNameChange(name: String) {
        newFamilyName = name
    }

    fun onFamilyDescriptionChange(description: String) {
        newFamilyDescription = description
    }

    fun onConceptNameChange(name: String) {
        newConceptName = name
    }

    fun onConceptDescriptionChange(description: String) {
        newConceptDescription = description
    }

    /**
     * Valida y guarda una nueva familia en la base de datos.
     */
    fun saveNewFamily() {
        // Regla de negocio: no guardar si el nombre está vacío.
        if (newFamilyName.isBlank()) {
            // Aquí podrías emitir un evento de error a la UI si quisieras
            return
        }

        viewModelScope.launch {
            val newFamily = FamilyEntity(
                name = newFamilyName,
                description = newFamilyDescription
            )
            familyDao.insertFamily(newFamily)
            // Limpiar formulario después de guardar
            newFamilyName = ""
            newFamilyDescription = ""
        }
    }

    /**
     * Valida y guarda un nuevo concepto formativo.
     */
    fun saveNewFormativeConcept() {
        if (newConceptName.isBlank()) return

        viewModelScope.launch {
            val newConcept = FormativeConceptEntity(
                name = newConceptName,
                description = newConceptDescription
            )
            conceptDao.insertFormativeConcept(newConcept)
            newConceptName = ""
            newConceptDescription = ""
        }
    }

    /**
     * Valida y guarda un nuevo concepto técnico asociado a una familia.
     */
    fun saveNewTechnicalConcept(familyId: Int) {
        if (newConceptName.isBlank()) return

        viewModelScope.launch {
            val newConcept = TechnicalConceptEntity(
                name = newConceptName,
                description = newConceptDescription,
                familyId = familyId
            )
            conceptDao.insertTechnicalConcept(newConcept)
            newConceptName = ""
            newConceptDescription = ""
        }
    }
}
