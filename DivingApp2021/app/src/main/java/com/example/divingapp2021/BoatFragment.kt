package com.example.divingapp2021

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.beust.klaxon.Klaxon
import com.example.divingapp2021.databinding.FragmentBoatBinding
import okhttp3.*
import java.io.IOException
import java.util.*


class BoatFragment : NavigationFragment<FragmentBoatBinding>() {
    private val adapter by lazy { BoatAdapter(listener) }
    private val listener = ItemClickListener()
    private val client = OkHttpClient()

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            if (intent?.action == "UPDATE_BOAT") {
                updateLayout()
            }
        }
    }

    override fun buildBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentBoatBinding {
        return FragmentBoatBinding.inflate(inflater, container, false)
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

        setTitle("List of boat")
        showBackButton(true)

        this.binding.statusCBStatisticsRecyclerView.adapter = adapter

        LocalBroadcastManager
            .getInstance(requireContext())
            .registerReceiver(broadCastReceiver, IntentFilter("UPDATE_BOAT"))

        this.binding.buttonInsert.setOnClickListener {
            FragmentHelper.addFragmentFromSide(
                requireActivity(),
                EditBoatFragment(insert = true),
                R.id.mainFrameLayout
            )
        }
        return rootview
    }


    override fun onResume() {
        super.onResume()
        updateLayout()
    }


    //Call db to get all boats and display them
    fun getAndShowBoats(url: String){
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Connection to webserver failed: " + e)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body!!.string()

                println("Successfully connected to webserver")
                println(json)

                val boats = Klaxon().parseArray<Boat>(json)

                //gain access to uiThread and show boats
                val mainHandler = Handler(requireContext().mainLooper)
                mainHandler.post {
                    adapter.submitList(boats)
                }

            }
        })
    }

    private fun updateLayout() {
        getAndShowBoats("http://10.0.2.2:8080/boats/")
    }

    private inner class ItemClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            val item = v.getTag(R.id.tag_item_item) as Boat
            FragmentHelper.addFragmentFromSide(
                requireActivity(),
                EditBoatFragment(boat = item),
                R.id.mainFrameLayout
            )
        }
    }
}
