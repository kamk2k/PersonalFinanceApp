package com.corellidev.personalfinance

import android.app.Application
import io.realm.Realm

/**
 * Created by Kamil on 2017-08-14.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this);
    }
}