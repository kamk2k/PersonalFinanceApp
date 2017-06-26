package com.corellidev.personalfinance

import dagger.Component
import javax.inject.Singleton

/**
 * Created by Kamil on 2017-06-22.
 */
@Singleton
@Component(modules = arrayOf(MainActivityModule::class))
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
}
