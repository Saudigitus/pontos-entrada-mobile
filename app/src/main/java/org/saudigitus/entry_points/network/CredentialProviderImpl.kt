package org.saudigitus.entry_points.network

import org.saudigitus.entry_points.data.local.PreferenceProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CredentialProviderImpl @Inject constructor(
    private val preferenceProvider: PreferenceProvider
) : CredentialProvider {
    override fun getUrl(): String {
        return preferenceProvider.getString("URL", URLMapping.BASE_URL).orEmpty()
    }

    override fun getUsername(): String {
        return preferenceProvider.getString("USERNAME", "").orEmpty()
    }

    override fun getPassword(): String {
        return preferenceProvider.getString("PASSWORD", "").orEmpty()
    }
}
