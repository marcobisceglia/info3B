package com.example.divingapp2021

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.beust.klaxon.Klaxon
import com.example.divingapp2021.databinding.FragmentEditBoatBinding
import okhttp3.*
import java.io.IOException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody


class EditBoatFragment(private val boat: Boat = Boat(), private val insert: Boolean = false) : NavigationFragment<FragmentEditBoatBinding>(){
        //,androidx.appcompat.view.ActionMode.Callback{

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
       // startSupportActionMode(getTitle(), this)

        if(insert){
            this.binding.buttonModify.visibility = View.GONE
            this.binding.buttonDelete.visibility = View.GONE

        }else{
            this.binding.modelEditText.hint = boat.model.toString()
            this.binding.seatsEditText.hint = boat.seats.toString()
            this.binding.seatsEditText.isEnabled = false
            this.binding.buttonInsert.visibility = View.GONE
        }

        this.binding.buttonDelete.setOnClickListener {
            //DELETE BOAT
            deleteBoat(boat.id)
        }

        this.binding.buttonModify.setOnClickListener {

            val boat = checkItemModify()
            if(boat!=null) {

                //MODIFY BOAT
                modifyBoat(boat)
            }
        }

        this.binding.buttonInsert.setOnClickListener {
            val boat = checkItem()
            if(boat!=null) {

                //CREATE BOAT
                createBoat(boat)
            }
        }

        return rootview
    }

    private fun checkItem():Boat?{
        val model = this.binding.modelEditText.text.toString()
        val seats = this.binding.seatsEditText.text.toString()
        val seatsAsNumber = seats.toIntOrNull()

        return if(model.isEmpty() || seats.isEmpty()){
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }
            builder?.apply {
                setMessage("Error")
                setTitle("Insert all values")
                setPositiveButton("OK",  DialogInterface.OnClickListener { dialog, id ->
                    // User clicked OK button
                })
                create()?.show()
            }
            null
        }else if(seatsAsNumber == null){
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }
            builder?.apply {
                setMessage("Error")
                setTitle("Seats must be a number")
                setPositiveButton("OK",  DialogInterface.OnClickListener { dialog, id ->
                    // User clicked OK button
                })
                create()?.show()
            }
            null
        }else
        {
            boat.model = model
            boat.seats = seatsAsNumber
            boat
        }
    }

    private fun checkItemModify():Boat?{
        if(this.binding.modelEditText.text.toString().isNotEmpty()) {
            boat.model = this.binding.modelEditText.text.toString()
        }else{
            boat.model = this.binding.modelEditText.hint.toString()
       }
        return boat
    }


    private val client = OkHttpClient()

    //Call db to delete boat
    fun deleteBoat(id: Int?){
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/boats/" + id)
            .delete()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Connection to webserver failed: " + e)
            }

            override fun onResponse(call: Call, response: Response) {
                println("Delete successful.")

                refreshBoats()
            }
        })
    }

    //Call db to modify boat
    fun modifyBoat(boat: Boat){
        val jsonBody = Klaxon().toJsonString(boat)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonBody.toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/boats/" + boat.id)
            .put(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Connection to webserver failed: " + e)
            }

            override fun onResponse(call: Call, response: Response) {
                println("Modified boat successfully.")

                refreshBoats()
            }
        })
    }

    //Call db to create boat
    fun createBoat(boat: Boat){
        val jsonBody = Klaxon().toJsonString(boat)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonBody.toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/boats/")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Connection to webserver failed: " + e)
            }

            override fun onResponse(call: Call, response: Response) {
                println("Created boat successfully.")

                refreshBoats()
            }
        })
    }

    fun refreshBoats(){
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post {
            FragmentHelper.popBackFragment(activity)
            LocalBroadcastManager.getInstance(requireActivity())
                .sendBroadcast(Intent("UPDATE_BOAT"))
        }
    }


    // :- ActionMode Callbacks
/*
    override fun onCreateActionMode(
            mode: androidx.appcompat.view.ActionMode?,
            menu: Menu?
    ): Boolean {
        mode?.menuInflater?.inflate(R.menu.menu_toolbar_edit, menu)

        return true
    }

    override fun onPrepareActionMode(
            mode: androidx.appcompat.view.ActionMode?,
            menu: Menu?
    ): Boolean {
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
        return false
    }

    override fun onActionItemClicked(
            mode: androidx.appcompat.view.ActionMode?,
            item: MenuItem?
    ): Boolean {
        selectedActionMenuItem = item

        when (item?.itemId) {
            R.id.confirmAction -> {
                /*   configurationService.setRelay(selectedRelay, relaysAdapterDetail.currentList)

                configurationViewModel.updateRelays(relaysAdapterDetail.currentList as List<AccordionItem>)

                mode?.finish()

                EpicApplicationUsageTracker.sendEvent("relay_" + getTitle(), "tap", "save")

                FragmentHelper.popBackFragment(activity)*/
            }
        }

        return false
    }

    override fun onDestroyActionMode(mode: androidx.appcompat.view.ActionMode?) {
        if (selectedActionMenuItem?.itemId != R.id.confirmAction) {
            if (selectedActionMenuItem?.itemId != R.id.confirmAction) {
                /*DialogHelper.showDialog(requireContext(),
                        getString(R.string.dialog_discard_changes_title),
                        getString(R.string.dialog_discard_changes_message),
                        {
                            EpicApplicationUsageTracker.sendEvent("relay_" + getTitle(), "tap", "cancel")

                            FragmentHelper.popBackFragment(activity)
                        },
                        {
                            startSupportActionMode(getTitle(), this)
                        })*/
            }
        }

        selectedActionMenuItem = null
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }
*/
}