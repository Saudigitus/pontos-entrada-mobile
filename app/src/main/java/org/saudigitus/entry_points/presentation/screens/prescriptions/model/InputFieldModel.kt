package org.saudigitus.entry_points.presentation.screens.prescriptions.model

data class InputFieldModel(
    val key: String,
    val ou: String? = null,
    val dataElement: String,
    val value: String,
    val conditionalValue: String? = null,
) {
    fun hasError() = try {
        if (conditionalValue != null)
            value.toInt() > conditionalValue.toInt()
        else false
    } catch (_: Exception) {
        false
    }
}
