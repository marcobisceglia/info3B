package com.example.divingapp2021

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText

import androidx.fragment.app.DialogFragment


class LoginFragment : DialogFragment() {

    // Use this instance of the interface to deliver action events
    lateinit var listener: NoticeDialogListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    interface NoticeDialogListener {
        fun onDialogPositiveClick(user: String, psw: String)
        //fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;
            val rootview = inflater.inflate(R.layout.dialog_sigin, null)
            val user = rootview.findViewById<EditText>(R.id.username)
            val psw = rootview.findViewById<EditText>(R.id.password)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(rootview)
                    // Add action buttons
                    .setPositiveButton("signin",
                            DialogInterface.OnClickListener { dialog, id ->
                                // sign in the user ...
                                // Send the positive button event back to the host activity

                                listener.onDialogPositiveClick(user = user.text.toString(), psw = psw.text.toString())
                            })
                    .setNegativeButton("cancel",
                            DialogInterface.OnClickListener { dialog, id ->
                                getDialog()?.cancel()
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}