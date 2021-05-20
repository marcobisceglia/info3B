package com.example.divingapp2021

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.divingapp2021.databinding.FragmentBookExcursionBinding
import com.example.divingapp2021.databinding.FragmentHomeBinding
import com.example.divingapp2021.databinding.FragmentUserBinding
import com.example.mylibrary.ProjectFragment
import com.google.android.material.datepicker.MaterialDatePicker

class BookExcursionFragment : NavigationFragment<FragmentBookExcursionBinding>()  {

    override fun buildBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentBookExcursionBinding {
        return FragmentBookExcursionBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val rootview = super.onCreateView(inflater, container, savedInstanceState).apply {
            isClickable = true
            isFocusable = true
        }
       // setTitle("HOME")
        showBackButton(false)

        this.binding.buttonDate.setOnClickListener {

                    }
        return rootview
    }

}