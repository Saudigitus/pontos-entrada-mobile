package org.saudigitus.entry_points.data.model

data class PrescriptionError(
    val uid: String,
    val name: String,
    val requestedQtd: Int,
    val givenQtd: Int,
)
