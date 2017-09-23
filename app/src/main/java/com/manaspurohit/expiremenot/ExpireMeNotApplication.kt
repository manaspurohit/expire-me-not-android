package com.manaspurohit.expiremenot

import android.app.Application
import io.realm.Realm

class ExpireMeNotApplication : Application() {

    lateinit var realmItem : Realm
        private set

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }

    fun openRealm() {
        realmItem = Realm.getDefaultInstance()
    }

    fun closeRealm() {
        realmItem.close()
    }
}