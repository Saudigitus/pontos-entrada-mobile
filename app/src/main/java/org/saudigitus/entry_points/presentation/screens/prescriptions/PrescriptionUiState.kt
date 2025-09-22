package org.saudigitus.entry_points.presentation.screens.prescriptions

import org.saudigitus.entry_points.data.model.Patient
import org.saudigitus.entry_points.data.model.Prescription
import org.saudigitus.entry_points.presentation.screens.prescriptions.model.BottomSheetState

data class PrescriptionUiState(
    val isLoading: Boolean = true,
    val displayErrors: Boolean = false,
    val isSaving: Boolean = false,
    val prescriptions: List<Prescription> = emptyList(),
    val errorState: BottomSheetState.ErrorState? = null,
    val saveState: BottomSheetState.SaveState? = null,
    val prescTei: Patient? = null,
    val isSaved: Boolean = false
)
