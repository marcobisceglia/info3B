package com.example.divingapp2021

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.mylibrary.ProjectFragment


abstract class NavigationFragment<B : ViewBinding> : ProjectFragment<B>() {
    var selectedActionMenuItem: MenuItem? = null

    private var title: String? = null
    private var isBackButton: Boolean = false


    fun setTitle(resourceId: Int) {
        this.title=getString(resourceId)
        updateTitle()
    }

    fun setTitle(title: String) {
        this.title=title
        updateTitle()
    }

    fun getTitle(): String {
        return title ?: ""
    }

    fun updateTitle() {
        //navigationViewModel.updateTitle(title ?: "")
    }

    fun showBackButton(show: Boolean) {
        isBackButton = show
       // navigationViewModel.showBackButton(show)
    }

    fun getShowBackButton(): Boolean {
        return isBackButton
    }

    fun startSupportActionMode(callback: ActionMode.Callback) {
        (activity as AppCompatActivity).startSupportActionMode(callback)
    }

    fun startSupportActionMode(title: String, callback: ActionMode.Callback) {
        val supportActionMode = (activity as AppCompatActivity).startSupportActionMode(callback)
        supportActionMode?.title = title
    }

}