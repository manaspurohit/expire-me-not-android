package com.manaspurohit.expiremenot.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.Date

open class Item(var name: String = "", var expireDate: Date = Date()) : RealmObject() {
    @PrimaryKey
    lateinit var itemId : String
    private set
}