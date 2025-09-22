package org.saudigitus.entry_points.data.model

data class MedicineIndicators(
    val completed: Pair<Int, Int>,
    val incomplete: Pair<Int, Int>,
    val zero: Pair<Int, Int>
)