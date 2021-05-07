package com.example.divingapp2021

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.divingapp2021.databinding.FragmentHomeBinding
import com.example.mylibrary.ProjectFragment

class HomeFragment : NavigationFragment<FragmentHomeBinding>()  {

    override fun buildBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
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

        this.binding.buttonLogin.setOnClickListener {
            if(isOwner()){
                FragmentHelper.addFragmentFromSide(
                        requireActivity(),
                        OwnerFragment(),
                        R.id.mainFrameLayout
                )
            }
        }
        return rootview
    }

    fun isOwner():Boolean{ //TODO
        return true
    }

}