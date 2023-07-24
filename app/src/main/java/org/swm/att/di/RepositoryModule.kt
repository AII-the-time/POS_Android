package org.swm.att.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.swm.att.data.remote.repository.AttPosRepositoryImpl
import org.swm.att.domain.repository.AttPosRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindPingRepository(
        pingRepositoryImpl: AttPosRepositoryImpl
    ): AttPosRepository

}