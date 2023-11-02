package org.swm.att.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.swm.att.common_ui.navigator.FakeAttNavigator
import org.swm.att.navigator.AttNavigator

@InstallIn(SingletonComponent::class)
@Module
interface NavigatorModule {
    @Binds
    fun bindAttNavigator(
        attNavigator: AttNavigator
    ): FakeAttNavigator
}