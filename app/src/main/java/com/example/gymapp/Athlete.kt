package com.example.gymapp

import android.net.Uri
import java.io.Serializable
import java.sql.Timestamp

val ATHLETE_EXTRA= "athleteExtra"
val ATHLETE_ID_EXTRA= "athleteIdExtra"

class Athlete: Serializable {
    var name : String
    var surname: String
    var birthDay: String
    var address: String
    var cap: String
    var phone: String
    var emailAddress: String


    constructor(
        name: String,
        surname: String,
        birthDay: String,
        address: String,
        cap: String,
        phone: String,
        emailAddress: String,
    ){
        this.name=name
        this.surname=surname
        this.birthDay=birthDay
        this.address=address
        this.cap=cap
        this.phone=phone
        this.emailAddress=emailAddress

    }


}