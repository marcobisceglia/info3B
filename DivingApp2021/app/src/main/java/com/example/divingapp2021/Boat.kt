package com.example.divingapp2021

open class Boat {
    var icon: Int? = null
    var name: String? = null
    var model: String? = null
    var places : String? = null


    override fun equals(other: Any?): Boolean {
        return other is Boat &&
                this.icon == other.icon &&
                this.places == other.places &&
                this.name == other.name &&
                this.model == other.model
    }
}