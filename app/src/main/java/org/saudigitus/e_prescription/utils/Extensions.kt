package org.saudigitus.e_prescription.utils

import org.saudigitus.e_prescription.data.model.Prescription
import org.saudigitus.e_prescription.data.model.PrescriptionError
import org.saudigitus.e_prescription.data.model.response.Attribute


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

