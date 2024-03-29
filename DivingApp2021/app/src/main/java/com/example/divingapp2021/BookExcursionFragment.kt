package com.example.divingapp2021

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.beust.klaxon.Klaxon
import com.example.divingapp2021.databinding.FragmentBookExcursionBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class BookExcursionFragment() : NavigationFragment<FragmentBookExcursionBinding>(){
    private val adapter by lazy { ExcursionAdapter(listener) }
    private val listener = ItemClickListener()
    private val client = OkHttpClient()
    private var trips: List<Trip>?= null

    private var choosenTrip: Trip?=null
    private var choosenTripView: View?=null

    val dialog = BookingDialogFragment()

    override fun buildBinding(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): FragmentBookExcursionBinding {
        return FragmentBookExcursionBinding.inflate(inflater, container, false)
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
        setTitle("Book an excursion")
        showBackButton(true)

        this.binding.triRecyclerView.adapter = adapter

        val today = Calendar.getInstance()
        val format = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        this.binding.datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            val month = month + 1
            val date = LocalDate.of(year, month, day)
            val dateString: String = format.format(date)
            getTrip(dateString)
        }
        this.binding.datePicker.minDate = today.timeInMillis

        getTrip(format.format(LocalDate.of(today.get(Calendar.YEAR), today.get(Calendar.MONTH)+1,
                today.get(Calendar.DAY_OF_MONTH))))


        this.binding.bookButton.setOnClickListener {
            val divers = this.binding.diversEditText.text.toString()
            val diversAsNumber = divers.toIntOrNull()

            if(diversAsNumber == null){
                val dialog = MessageDialog(MessageDialog.DIALOG_MODE.DIVERS_NOT_NUMBER)
                dialog.show(requireActivity().supportFragmentManager, "not a number")
            }else{
                bookExcursion(diversAsNumber)
            }

        }
        return rootview
    }

    private inner class ItemClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            val item = v.getTag(R.id.tag_item_item) as Trip
            if(choosenTrip == null || choosenTrip!=item){
                v.setBackgroundColor(resources.getColor(R.color.colorGrey60))
                choosenTripView?.setBackgroundColor(resources.getColor(R.color.listItemBackground))

                //select new trip
                choosenTrip = item
                choosenTripView = v
            }
        }
    }

    private fun bookExcursion(num: Int){
        val u = Booking()
        u.user = MyProperties.instance!!.userLogged
        u.numDivers = num
        u.trip = choosenTrip

        val jsonBody = Klaxon().toJsonString(u.createBooking())

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonBody.toRequestBody(mediaType)
        val request = Request.Builder()
                .url("http://10.0.2.2:8080/bookings/")
                .post(body)
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Connection to webserver failed: " + e)
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body!!.string()

                println("Successfully connected to webserver")
                println(json)

                //status:405 barche piene
                //status:400 bad request booking already exist

                val mainHandler = Handler(Looper.getMainLooper())
                mainHandler.post {
                    val args = Bundle()
                    args.putInt("code", response.code)
                    args.putString("dateTime", u.trip?.dateTimeString)
                    args.putString("divers", num.toString())
                    dialog.arguments = args
                    dialog.show(requireActivity().supportFragmentManager, "NoticeDialogFragment")
                }

            }
        })
    }

    private fun getTrip(date: String){
        val request = Request.Builder()
                .url("http://10.0.2.2:8080//trips?date=$date")
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Connection to webserver failed: " + e)
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(call: Call, response: Response) {
                val json = response.body!!.string()

                println("Successfully connected to webserver")
                println(json)

                trips = Klaxon().parseArray(json)

                trips?.forEach { it.convertDate() }

                val mainHandler = Handler(Looper.getMainLooper())
                mainHandler.post {
                    if (trips?.isNotEmpty() == true) {
                        adapter.submitList(trips)
                        binding.tripFound.visibility = View.VISIBLE
                        binding.noTripFound.visibility = View.GONE
                    } else {
                        binding.noTripFound.visibility = View.VISIBLE
                        binding.tripFound.visibility = View.GONE
                    }

                }

            }
        })
    }

}