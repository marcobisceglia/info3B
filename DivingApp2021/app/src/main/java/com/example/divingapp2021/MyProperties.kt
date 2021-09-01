package com.example.divingapp2021

class MyProperties protected constructor() {
    var userLogged: User?=null

    companion object {
        private var mInstance: MyProperties? = null

        @get:Synchronized
        val instance: MyProperties?
            get() {
                if (null == mInstance) {
                    mInstance = MyProperties()
                }
                return mInstance
            }

    }
}