package com.example.coinapp

import android.app.Application
import com.example.coinapp.di.component.DaggerCoinAppComponent

class CoinApp : Application() {

    val component by lazy {
        DaggerCoinAppComponent.factory().create(this)
    }
}