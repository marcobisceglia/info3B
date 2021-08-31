package com.example.divingapp2021

class User {
    var id: Int? = null
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var password: String? = null
    var license: String? = null
    var loggedIn: Boolean? = null
    var userRole: ROLE? = null

    enum class ROLE{
        USER, ADMIN
    }
}