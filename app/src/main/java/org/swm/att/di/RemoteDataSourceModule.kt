package org.swm.att.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.swm.att.data.remote.datasource.AttEncryptedPrefDataSource
import org.swm.att.data.remote.datasource.MenuDataSource
import org.swm.att.data.remote.datasource.OrderDataSource
import org.swm.att.data.remote.service.AttPosService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

    @Provides
    @Singleton
    fun provideMenuDataSource(attPosService: AttPosService): MenuDataSource {
        return MenuDataSource(attPosService)
    }

    @Provides
    @Singleton
    fun provideAttEncryptedPrefDataSource (attEncryptedPref: SharedPreferences): AttEncryptedPrefDataSource {
        return AttEncryptedPrefDataSource(attEncryptedPref)
    }

    @Provides
    @Singleton
    fun providePayDataSource(attPosService: AttPosService): OrderDataSource {
        return OrderDataSource(attPosService)
    }

}