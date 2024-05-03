package com.example.caretaker.models

data class VolunteerApplication(
    var name: String?=null,
    var age: String?=null,
    var gender: String?=null,
    var contact: String?=null,
    var address: String?=null,
    var location: String?=null,
    var service: String?=null,
    var shift:String?=null,
    var amount: String?=null,
    val desc:String?=null,
    val hiredBy:String?=null,
    var hireStatus:String?=null,
    var uid:String?=null
)
