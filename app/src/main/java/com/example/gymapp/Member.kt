package com.example.gymapp

import java.sql.Timestamp

class Member {
    var name : String? = null
    var surname: String? = null
    var birthDay: String? = null
    var address: String? = null
    var cap: String? = null
    var phone: String? = null
    var emailAddress: String? = null


    //constuctor(){}

    constructor(name: String?,
                surname: String?,
                birthDay: String?,
                address: String?,
                cap: String?,
                phone: String?,
                emailAddress: String?){
        this.name=name
        this.surname=surname
        this.birthDay=birthDay
        this.address=address
        this.cap=cap
        this.phone=phone
        this.emailAddress=emailAddress


    }


}