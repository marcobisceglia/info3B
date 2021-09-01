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


class MessageDialog(val mode: DIALOG_MODE) : DialogFragment() {
    enum class DIALOG_MODE{
        LOGOUT,
        PSW_NOT_CORRECT,
        USER_NOT_FOUND,
        DIVERS_NOT_NUMBER,
        SEATS_NOT_NUMBER
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;
            val rootview = inflater.inflate(R.layout.dialog_message, null)

            val pswNotCorrect = rootview.findViewById<LinearLayout>(R.id.pswNotCorrectLayout)
            val noUser = rootview.findViewById<LinearLayout>(R.id.noUserFoundLayout)
            val logout = rootview.findViewById<LinearLayout>(R.id.logoutLayout)
            val numError = rootview.findViewById<LinearLayout>(R.id.numErrorLayout)
            val numSeats = rootview.findViewById<LinearLayout>(R.id.numErrorBoatsLayout)

            if(mode ==DIALOG_MODE.LOGOUT){
                logout.visibility = View.VISIBLE
                pswNotCorrect.visibility = View.GONE
                noUser.visibility = View.GONE
                numError.visibility = View.GONE
                numSeats.visibility = View.GONE
            }else if(mode ==DIALOG_MODE.PSW_NOT_CORRECT){
                logout.visibility = View.GONE
                pswNotCorrect.visibility = View.VISIBLE
                noUser.visibility = View.GONE
                numError.visibility = View.GONE
                numSeats.visibility = View.GONE
            }else if(mode ==DIALOG_MODE.USER_NOT_FOUND){
                logout.visibility = View.GONE
                pswNotCorrect.visibility = View.GONE
                noUser.visibility = View.VISIBLE
                numError.visibility = View.GONE
                numSeats.visibility = View.GONE
            }else if(mode ==DIALOG_MODE.DIVERS_NOT_NUMBER){
                logout.visibility = View.GONE
                pswNotCorrect.visibility = View.GONE
                noUser.visibility = View.GONE
                numError.visibility = View.VISIBLE
                numSeats.visibility = View.GONE
            }else if(mode ==DIALOG_MODE.SEATS_NOT_NUMBER){
                logout.visibility = View.GONE
                pswNotCorrect.visibility = View.GONE
                noUser.visibility = View.GONE
                numError.visibility = View.GONE
                numSeats.visibility = View.VISIBLE
            }

           // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(rootview)
                    // Add action buttons
                    .setPositiveButton("OK", null)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}