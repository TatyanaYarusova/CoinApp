package com.example.coinapp.di.module

import android.app.Application
import com.example.coinapp.ui.adapter.CoinAdapter
import dagger.Module
import dagger.Provides

@Module
interface UiModule {
    companion object {
        @Provides
        fun providerAdapter(application: Application) = CoinAdapter(application)
    }
}