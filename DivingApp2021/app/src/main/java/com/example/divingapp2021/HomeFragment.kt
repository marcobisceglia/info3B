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

        showBackButton(false)

        this.binding.buttonLogin.setOnClickListener {
                getUser(this.binding.username.text.toString(), this.binding.password.text.toString())
        }
        return rootview
    }

    private fun isOwner(user: User):Boolean{
       return user.userRole==User.ROLE.ADMIN
    }

    //Call db to get all boats and display them
    private fun getUser(user: String, psw: String){
        val u = User()
        u.email = user
        u.password =  psw
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
                            if(isOwner(u)){
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
                }else if(response.code == 401) {
                    //psw not correct
                    val dialog = MessageDialog(MessageDialog.DIALOG_MODE.PSW_NOT_CORRECT)
                    dialog.show(requireActivity().supportFragmentManager, "Psw not correct")
                }else if(response.code == 404){
                    //user email not found
                    val dialog = MessageDialog(MessageDialog.DIALOG_MODE.USER_NOT_FOUND)
                    dialog.show(requireActivity().supportFragmentManager, "User not found")
                }
            }
        })
    }
}