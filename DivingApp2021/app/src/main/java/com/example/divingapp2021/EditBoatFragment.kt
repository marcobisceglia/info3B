package com.example.divingapp2021

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.divingapp2021.databinding.FragmentEditBoatBinding


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
            this.binding.modelEditText.hint = boat.model
            //this.binding.nameEditText.hint = boat.name
            this.binding.seatsEditText.hint = boat.seats.toString()
            this.binding.buttonInsert.visibility = View.GONE
        }

        this.binding.buttonDelete.setOnClickListener {
            //TODO ELIMINA BOAT DAL DB
            FragmentHelper.popBackFragment(activity)
            LocalBroadcastManager.getInstance(requireActivity())
                    .sendBroadcast(Intent("UPDATE_BOAT"))

        }

        this.binding.buttonModify.setOnClickListener {
            val boat = checkItem()
            if(boat!=null) {
                //todo modifica solo il nome 

                //TODO CONFERMA MODIFICHE ITEM AL DB
                    //i valori da inserire nel DB sono contenuti in boat
                FragmentHelper.popBackFragment(activity)
                LocalBroadcastManager.getInstance(requireActivity())
                        .sendBroadcast(Intent("UPDATE_BOAT"))
            }
        }

        this.binding.buttonInsert.setOnClickListener {
            val boat = checkItem()
            if(boat!=null) {
                //TODO INSERIMENTO NUOVO ITEM AL DB
                //i valori da inserire nel DB sono contenuti in boat
                FragmentHelper.popBackFragment(activity)
                LocalBroadcastManager.getInstance(requireActivity())
                        .sendBroadcast(Intent("UPDATE_BOAT"))
            }
        }

        return rootview
    }

    private fun checkItem():Boat?{
        val boat = Boat()
        val model = if ( this.binding.modelEditText.text.toString().isEmpty()) {
            this.binding.modelEditText.hint
        } else {
            this.binding.modelEditText.text.toString()
        } //se il testo non è nullo gli metto ciò che era hint
        val name = if ( this.binding.nameEditText.text.toString().isEmpty()) {
            this.binding.nameEditText.hint
        } else {
            this.binding.nameEditText.text.toString()
        }
        val seats = if ( this.binding.seatsEditText.text.toString().isEmpty()) {
            this.binding.seatsEditText.hint
        } else {
            this.binding.seatsEditText.text.toString()
        }

        return if(model==null || name==null || seats==null){
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
        }else{
            boat.model = model.toString()
            //boat.name = name.toString()
            boat.seats = seats.toString().toInt()
            boat
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