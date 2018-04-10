package ru.example.simbirsoft.models

import com.google.firebase.database.IgnoreExtraProperties

/**
 * Created by harri
 * on 10.03.2018.
 */

@IgnoreExtraProperties
class User {

    var avatar = ""
    var email = ""
    var name = ""
    var phone = ""
    var lat = .0
    var lng = .0

    constructor()
    constructor(avatar: String?, email: String?, name: String?, phone: String?, lat: Double?,
                lng: Double?) {
        this.avatar = avatar?: ""
        this.email = email ?: ""
        this.name = name ?: ""
        this.phone = phone ?: ""
        this.lat = lat ?: .0
        this.lng = lng ?: .0
    }
}