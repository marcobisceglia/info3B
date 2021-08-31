package com.example.divingapp2021

import android.os.Build
import androidx.annotation.RequiresApi
import com.beust.klaxon.Converter
import com.beust.klaxon.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class Trip {
    val id: Int?= null
    @Json(name = "dateTime")
    val dateTimeString: String?=null
    @Json(ignored = true)
    var dateTime: LocalDateTime?=null

    fun convertDate() {
       // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        dateTime = LocalDateTime.parse(this.dateTimeString, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
    }
}