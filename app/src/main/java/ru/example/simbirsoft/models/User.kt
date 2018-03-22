package ru.example.simbirsoft.models

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by harri
 * on 10.03.2018.
 */

@IgnoreExtraProperties
class User {

    lateinit var avatar: String
    lateinit var email: String
    lateinit var name: String
    lateinit var phone: String

    constructor()
    constructor(avatar: String?, email: String?, name: String?, phone: String?) {
        this.avatar = avatar?: ""
        this.email = email ?: ""
        this.name = name ?: ""
        this.phone = phone ?: ""
    }
}