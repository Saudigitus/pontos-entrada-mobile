package org.saudigitus.entry_points.network

interface CredentialProvider {
    fun getUrl(): String
    fun getUsername(): String
    fun getPassword(): String
}