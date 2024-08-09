package com.example.coinapp.di.component

import android.app.Application
import com.example.coinapp.di.annotation.AppScope
import com.example.coinapp.di.module.DataModule
import com.example.coinapp.di.module.ViewModelModule
import com.example.coinapp.ui.DetailsFragment
import com.example.coinapp.ui.MainFragment
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class
    ]
)

@AppScope
interface CoinAppComponent {

    @Component.Factory
    interface CoinAppComponentFactory {
        fun create(
            @BindsInstance application: Application
        ): CoinAppComponent
    }

    fun inject(fragment: MainFragment)

    fun inject(fragment: DetailsFragment)
}