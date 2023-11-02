package org.swm.att.common_ui.navigator

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface AttNavigatorInjector {
    fun attNavigator(): FakeAttNavigator
}
