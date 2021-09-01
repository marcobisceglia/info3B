package com.example.divingapp2021

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.beust.klaxon.Klaxon
import com.example.divingapp2021.databinding.FragmentHomeBinding
import com.example.mylibrary.ProjectFragment
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

//class HomeFragment : NavigationFragment<FragmentHomeBinding>(), LoginFragment.NoticeDialogListener  {
class HomeFragment : NavigationFragment<FragmentHomeBinding>()  {
    private val client = OkHttpClient()

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
                getUser(this.binding.username.text.toString(), this.binding.password.text.toString())
                // Create an instance of the dialog fragment and show it
              //  val dialog = LoginFragment()
             //   dialog.listener = this
           //     dialog.show(requireActivity().supportFragmentManager, "NoticeDialogFragment")

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

    private fun isOwner(user: String?, psw: String?):Boolean{
        return user=="email1@email.com" && psw=="password" //TODO USANDO USER ROLE 
    }

    //Call db to get all boats and display them
    private fun getUser(user: String, psw: String){
        val u = User()
        u.email = "email1@email.com"
        u.password =  "password"
        val jsonBody = Klaxon().toJsonString(u)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonBody.toRequestBody(mediaType)
        val request = Request.Builder()
                .url("http://10.0.2.2:8080/users/login/")
                .post(body)
                .build()


        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Connection to webserver failed: " + e)
                //binding.noUserFound.visibility = View.VISIBLE
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body!!.string()

                println("Successfully connected to webserver")
                println(json)

                if(response.code == 200){
                    val userlogged = Klaxon().parse<User>(json)
                    if(userlogged!=null){
                        MyProperties.instance!!.userLogged = userlogged
                        activity?.runOnUiThread {
                            if(isOwner(u.email, u.password)){
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
                }else{
                    //TODO ERROR LOGIN
                }
            }
        })
    }
}