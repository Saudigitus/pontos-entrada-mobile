package org.saudigitus.entry_points.presentation.screens.prescriptions

import org.saudigitus.entry_points.data.model.Prescription

sealed class PrescriptionUiEvent {
    data class OnPrescriptionValueChange(val prescription: Prescription, val value: String): PrescriptionUiEvent()
    data object OnTempSave : PrescriptionUiEvent()
    data object OnSave : PrescriptionUiEvent()
    data object OnBack: PrescriptionUiEvent()
    data object CloseErrorBottomSheet: PrescriptionUiEvent()
    data object CloseSaveBottomSheet: PrescriptionUiEvent()
}