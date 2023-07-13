package org.swm.att.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.swm.att.data.remote.datasource.PingDataSource
import org.swm.att.data.remote.service.PingService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

    @Provides
    @Singleton
    fun providePingDataSource(pingService: PingService): PingDataSource {
        return PingDataSource(pingService)
    }

}