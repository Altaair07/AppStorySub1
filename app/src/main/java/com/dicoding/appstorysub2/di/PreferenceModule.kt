package com.dicoding.appstorysub2.di


import android.content.Context
import android.content.SharedPreferences
import com.dicoding.appstorysub2.R
import com.dicoding.appstorysub2.data.local.sharedpref.SessionManager
import com.dicoding.appstorysub2.data.local.sharedpref.SessionManagerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferenceModule {

    @Binds
    @Singleton
    abstract fun provideSessionManager(sessionManagerImpl: SessionManagerImpl): SessionManager

    companion object {
        @Provides
        @Singleton
        fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
            return context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        }
    }
}