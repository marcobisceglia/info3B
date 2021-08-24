package com.example.divingapp2021

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.divingapp2021.databinding.FragmentOwnerBinding

class OwnerFragment : NavigationFragment<FragmentOwnerBinding>()  {

    override fun buildBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentOwnerBinding {
        return FragmentOwnerBinding.inflate(inflater, container, false)
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
        setTitle("Owner")
        showBackButton(true)

        this.binding.buttonBoat.setOnClickListener {
            FragmentHelper.addFragmentFromSide(
                requireActivity(),
               BoatFragment(),
                R.id.mainFrameLayout
            )
        }
        this.binding.buttonTrip.setOnClickListener {
            FragmentHelper.addFragmentFromSide(
                requireActivity(),
                BoatFragment(),
                R.id.mainFrameLayout
            )
        }
        this.binding.buttonUser.setOnClickListener {
            FragmentHelper.addFragmentFromSide(
                requireActivity(),
                BoatFragment(),
                R.id.mainFrameLayout
            )
        }

        return rootview
    }

}