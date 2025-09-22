package org.saudigitus.entry_points.utils

import org.saudigitus.entry_points.data.model.Prescription
import org.saudigitus.entry_points.data.model.PrescriptionError
import org.saudigitus.entry_points.data.model.response.Attribute
import org.saudigitus.entry_points.presentation.screens.prescriptions.model.InputFieldModel


fun Prescription.toPrescriptionError(givenQtd: Int) =
    PrescriptionError(
        uid = this.uid,
        name = this.name,
        requestedQtd = this.requestedQtd,
        givenQtd = givenQtd
    )

fun List<Attribute>.getByAttr(attr: String): Pair<String, String> {
    val attr = find { it.attribute == attr }

    return Pair(attr?.displayName.orEmpty(), attr?.value.orEmpty())
}

fun List<Prescription>.generateFieldModel() =
    mapNotNull {
        if (it.completedQtd > 0) {
            InputFieldModel(
                key = it.uid,
                ou = it.ou,
                dataElement = UIDMapping.DATA_ELEMENT_QTD_GIVEN,
                value = "${it.completedQtd}",
                conditionalValue = "${it.requestedQtd}",
            )
        } else null
    }
