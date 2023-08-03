package org.swm.att.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.swm.att.data.remote.service.AttPosService
import org.swm.att.data.remote.service.AttPosUserService
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object AttPosServiceModule {

    @Provides
    fun provideAttPosService(retrofit: Retrofit): AttPosService =
        retrofit.create(AttPosService::class.java)

    @Provides
    fun provideAttPosUserService(retrofit: Retrofit): AttPosUserService =
        retrofit.create(AttPosUserService::class.java)

}