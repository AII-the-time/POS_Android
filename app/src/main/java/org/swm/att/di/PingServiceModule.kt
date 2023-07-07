package org.swm.att.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.swm.att.data.remote.service.PingService
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PingServiceModule {

    @Singleton
    @Provides
    fun providePingService(retrofit: Retrofit): PingService =
        retrofit.create(PingService::class.java)

}