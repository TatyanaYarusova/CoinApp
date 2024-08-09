package com.example.coinapp.di.module

import androidx.lifecycle.ViewModel
import com.example.coinapp.di.annotation.ViewModelKey
import com.example.coinapp.presentation.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(viewModel: MainViewModel): ViewModel

}