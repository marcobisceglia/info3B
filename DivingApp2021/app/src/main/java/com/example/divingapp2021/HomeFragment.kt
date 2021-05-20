package com.example.divingapp2021

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.divingapp2021.databinding.FragmentHomeBinding
import com.example.mylibrary.ProjectFragment

class HomeFragment : NavigationFragment<FragmentHomeBinding>(), LoginFragment.NoticeDialogListener  {

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

                // Create an instance of the dialog fragment and show it
                val dialog = LoginFragment()
                dialog.listener = this
                dialog.show(requireActivity().supportFragmentManager, "NoticeDialogFragment")

            /*
            if(isOwner()){
                FragmentHelper.addFragmentFromSide(
                        requireActivity(),
                        OwnerFragment(),
                        R.id.mainFrameLayout
                )
            }*/
        }
        return rootview
    }

    private fun isOwner(user: String, psw: String):Boolean{ //TODO
        return user=="GG" && psw=="GG"
    }


    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface

    override fun onDialogPositiveClick(user: String, psw: String) {
        if(isOwner(user, psw)) {
            FragmentHelper.addFragmentFromSide(
                    requireActivity(),
                    OwnerFragment(),
                    R.id.mainFrameLayout
            )
        }else{
            FragmentHelper.addFragmentFromSide(
                    requireActivity(),
                    UserFragment(),
                    R.id.mainFrameLayout
            )
        }
    }
}