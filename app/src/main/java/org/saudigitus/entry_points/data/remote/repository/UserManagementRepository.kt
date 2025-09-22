package org.saudigitus.entry_points.data.remote.repository

import org.saudigitus.entry_points.data.Result

interface UserManagerRepository {

    suspend fun login(server: String, username: String, password: String): Result<Boolean>

    suspend fun isLoggedIn(): Result<Boolean>

    suspend fun userName(): Result<String?>

    suspend fun logout()
}