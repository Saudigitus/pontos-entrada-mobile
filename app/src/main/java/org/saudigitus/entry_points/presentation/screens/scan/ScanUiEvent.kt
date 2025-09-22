package org.saudigitus.entry_points.presentation.screens.scan

sealed class ScanUiEvent {
    data class Scan(val scanResult: String): ScanUiEvent()
    data class NavTo(val route: String): ScanUiEvent()
    data object Logout: ScanUiEvent()
}