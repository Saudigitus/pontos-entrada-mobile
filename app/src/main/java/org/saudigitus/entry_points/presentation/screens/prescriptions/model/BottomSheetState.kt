package org.saudigitus.entry_points.presentation.screens.prescriptions.model

import androidx.compose.runtime.Immutable
import org.saudigitus.entry_points.data.model.MedicineIndicators
import org.saudigitus.entry_points.data.model.PrescriptionError

@Immutable
sealed class BottomSheetState(
    open val isLoading: Boolean
) {
    @Immutable
    data class ErrorState(
        override val isLoading: Boolean,
        val prescriptions: List<PrescriptionError> = emptyList()
    ): BottomSheetState(isLoading)

    @Immutable
    data class SaveState(
        override val isLoading: Boolean,
        val indicators: MedicineIndicators
    ): BottomSheetState(isLoading)
}