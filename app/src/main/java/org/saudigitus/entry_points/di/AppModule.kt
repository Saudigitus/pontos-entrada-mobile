package org.saudigitus.entry_points.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import org.saudigitus.entry_points.data.local.PreferenceProvider
import org.saudigitus.entry_points.data.local.repository.PreferenceProviderImpl
import org.saudigitus.entry_points.data.remote.repository.PrescriptionRepository
import org.saudigitus.entry_points.data.remote.repository.UserManagerRepository
import org.saudigitus.entry_points.data.remote.repository.impl.PrescriptionRepositoryImpl
import org.saudigitus.entry_points.data.remote.repository.impl.UserManagerRepositoryImpl
import org.saudigitus.entry_points.network.HttpClientHelper
import org.saudigitus.entry_points.network.NetworkUtils
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesUserManagerImpl(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        httpClientHelper: HttpClientHelper,
        networkUtils: NetworkUtils,
        preferenceProvider: PreferenceProvider
    ): UserManagerRepository = UserManagerRepositoryImpl(
        networkUtils,
        httpClientHelper,
        preferenceProvider,
        ioDispatcher
    )

    @Provides
    @Singleton
    fun providesPreferenceProvider(@ApplicationContext context: Context): PreferenceProvider =
        PreferenceProviderImpl(context)

    @Provides
    @Singleton
    fun providePrescriptionRepository(
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        httpClientHelper: HttpClientHelper,
        networkUtils: NetworkUtils
    ): PrescriptionRepository = PrescriptionRepositoryImpl(networkUtils, httpClientHelper,  ioDispatcher)
}