package com.corellidev.personalfinance

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by Kamil on 2017-08-14.
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this);
        val realmConfig = RealmConfiguration.Builder()
                //TODO remove before publishing
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfig)
    }
}