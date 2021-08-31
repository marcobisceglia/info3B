package com.example.divingapp2021

class Booking {
    val id:Int?=null
    var trip: Trip?=null
    var user: User?=null
    var numDivers: Int?=null

    fun createBooking(): fastBooking{
        val temp = fastBooking()
        temp.tripId =this.trip?.id
        temp.userId = this.user?.id
        temp.numPeople = this.numDivers
        return temp
    }

    class fastBooking{
        var tripId: Int?=null
        var userId: Int?=null
        var numPeople: Int?=null
    }
}