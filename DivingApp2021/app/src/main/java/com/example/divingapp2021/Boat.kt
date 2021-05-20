package com.example.divingapp2021

open class Boat {
    var id: Int? = null
    var icon: Int? = null
    var model: String? = null
    var seats : Int? = null

    override fun equals(other: Any?): Boolean {
        return other is Boat &&
                this.id == other.id &&
                this.icon == other.icon &&
                this.seats == other.seats &&
                this.model == other.model
    }
}