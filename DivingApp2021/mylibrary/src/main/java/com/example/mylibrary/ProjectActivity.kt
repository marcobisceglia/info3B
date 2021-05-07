package com.example.mylibrary

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class ProjectActivity <B : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: B

    protected abstract fun buildBinding(inflater: LayoutInflater): B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //
        this.binding = this.buildBinding(this.layoutInflater)
        this.setContentView(this.binding.root)
    }
}