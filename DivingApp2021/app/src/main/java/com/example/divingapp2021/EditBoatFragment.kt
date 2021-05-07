package com.example.divingapp2021

import android.os.Bundle
import android.view.*
import com.example.divingapp2021.databinding.FragmentEditBoatBinding

class EditBoatFragment(private val boat: Boat) : NavigationFragment<FragmentEditBoatBinding>(){

    override fun buildBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentEditBoatBinding {
        return FragmentEditBoatBinding.inflate(inflater, container, false)
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
        setTitle("EDIT BOAT")
        showBackButton(true)
        this.binding.modelEditText.hint = boat.model
        this.binding.nameEditText.hint = boat.name
        this.binding.placesEditText.hint = boat.places

        return rootview
    }

}