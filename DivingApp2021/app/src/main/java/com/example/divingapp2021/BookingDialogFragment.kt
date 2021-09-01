package com.example.divingapp2021

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import okhttp3.OkHttpClient


class BookingDialogFragment : DialogFragment() {
    private val client = OkHttpClient()
    // Use this instance of the interface to deliver action events
    //lateinit var listener: NoticeDialogListener

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
   /* interface NoticeDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
    }*/

    //status:405 barche piene
    //status:400 bad request booking already exist

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;
            val rootview = inflater.inflate(R.layout.dialog_booking, null)

            val args = arguments
            val code = args!!.getInt("code")
            val date = args.getString("dateTime")
            val divers = args.getString("divers")


            val normal = rootview.findViewById<LinearLayout>(R.id.normalLayout)
            val bookExist = rootview.findViewById<LinearLayout>(R.id.bookExistLayout)
            val boatsFull = rootview.findViewById<LinearLayout>(R.id.boatsFullLayout)

            if(code==405){
                normal.visibility = View.GONE
                bookExist.visibility = View.GONE
                boatsFull.visibility = View.VISIBLE
            }else if(code==400){
                normal.visibility = View.GONE
                bookExist.visibility = View.VISIBLE
                boatsFull.visibility = View.GONE
            }else{
                normal.visibility = View.VISIBLE
                bookExist.visibility = View.GONE
                boatsFull.visibility = View.GONE
                //set param
                rootview.findViewById<TextView>(R.id.dateTextView).text = "Date: " + date
                rootview.findViewById<TextView>(R.id.diversTextView).text = "N. divers: " + divers
            }


           // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(rootview)
                    // Add action buttons
                    .setPositiveButton("OK", null)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDismiss(dialog: DialogInterface) {
        FragmentHelper.popBackFragment(requireActivity())
    }
}