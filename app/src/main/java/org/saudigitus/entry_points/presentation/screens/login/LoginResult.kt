package org.saudigitus.entry_points.presentation.screens.login

data class LoginResult(
    val success: Boolean = false,
    val error: String? = null
)