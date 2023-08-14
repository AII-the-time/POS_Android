package org.swm.att.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.swm.att.data.repository.AttMenuRepositoryImpl
import org.swm.att.data.repository.AttPosUserRepositoryImpl
import org.swm.att.domain.repository.AttMenuRepository
import org.swm.att.domain.repository.AttPosUserRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindAttMenuRepository(
        pingRepositoryImpl: AttMenuRepositoryImpl
    ): AttMenuRepository

    @Binds
    fun bindAttPosUserRepository(
        attPosUserRepositoryImpl: AttPosUserRepositoryImpl
    ): AttPosUserRepository

}