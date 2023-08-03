package org.swm.att.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.prefs.Preferences
import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DataStoreModule {
//
//    @Singleton
//    @Provides
//    fun providePreferencesDataStore(@ApplicationContext context): DataStore<Preferences> =
//        PreferenceDataStoreFactory.create
//
//}