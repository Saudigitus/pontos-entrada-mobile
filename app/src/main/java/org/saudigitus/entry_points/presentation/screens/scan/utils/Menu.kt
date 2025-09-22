package org.saudigitus.entry_points.presentation.screens.scan.utils

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.ui.graphics.vector.ImageVector
import org.saudigitus.entry_points.R
import org.saudigitus.entry_points.presentation.screens.scan.ScanUiEvent

data class MenuItem(
    val icon: ImageVector,
    @StringRes val name: Int,
    val event: ScanUiEvent
)

object Menu {
    val items = listOf(
        MenuItem(
            icon = Icons.AutoMirrored.Filled.Logout,
            name = R.string.logout,
            event = ScanUiEvent.Logout
        ),
    )
}