package com.example.coinapp.di.module

import com.example.coinapp.data.api.ApiFactory
import com.example.coinapp.data.api.ApiService
import com.example.coinapp.data.repository.CoinDetailsRepositoryImpl
import com.example.coinapp.data.repository.CoinListRepositoryImpl
import com.example.coinapp.di.annotation.AppScope
import com.example.coinapp.domain.repository.CoinDetailsRepository
import com.example.coinapp.domain.repository.CoinListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
interface DataModule {

    @Binds
    @AppScope
    fun bindCoinListRepository(impl: CoinListRepositoryImpl): CoinListRepository

    @Binds
    @AppScope
    fun bindCoinDetailsRepository(impl: CoinDetailsRepositoryImpl): CoinDetailsRepository

    companion object {
        @Provides
        fun provideApiService(): ApiService = ApiFactory.apiService

        @Provides
        fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO
    }
}