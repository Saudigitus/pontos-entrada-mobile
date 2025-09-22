package org.saudigitus.entry_points.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.saudigitus.entry_points.data.local.PreferenceProvider
import org.saudigitus.entry_points.network.CredentialProvider
import org.saudigitus.entry_points.network.CredentialProviderImpl
import org.saudigitus.entry_points.network.HttpClientHelper
import org.saudigitus.entry_points.network.NetworkUtils
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideCredentialProvider(
        preferenceProvider: PreferenceProvider
    ): CredentialProvider = CredentialProviderImpl(preferenceProvider)

    @Provides
    @Singleton
    fun provideHttpClientHelper(
        credentialProvider: CredentialProvider
    ): HttpClientHelper = HttpClientHelper(
        credentialProvider
    )

    @Provides
    @Singleton
    fun providesNetworkUtils(@ApplicationContext context: Context): NetworkUtils {
        return NetworkUtils(context)
    }
}