package com.example.divingapp2021

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.divingapp2021.databinding.FragmentBookExcursionBinding
import com.example.divingapp2021.databinding.FragmentHomeBinding
import com.example.divingapp2021.databinding.FragmentUserBinding
import com.example.mylibrary.ProjectFragment
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*
import kotlin.collections.ArrayList

class BookExcursionFragment : NavigationFragment<FragmentBookExcursionBinding>()  {
    private val adapter by lazy { ExcursionAdapter(listener) }
    private val listener = ItemClickListener()

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
       // setTitle("HOME")
        showBackButton(false)

        this.binding.triRecyclerView.adapter = adapter
        val trips = listOf<String>("dv", "xfbfg", "khjkj", "gvjhv")

        val today = Calendar.getInstance()
        this.binding.datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->
            val month = month + 1
            //TODO CHIAMATA PER VEDERE LE ESCURSIONI DISPONIBILI
            if(day == 10){
                adapter.submitList(trips)
                this.binding.tripFound.visibility=View.VISIBLE
                this.binding.noTripFound.visibility=View.GONE
            }else{
                this.binding.noTripFound.visibility = View.VISIBLE
                this.binding.tripFound.visibility=View.GONE
            }
            val msg = "You Selected: $day/$month/$year"
            println( msg)
        }
        this.binding.datePicker.minDate = today.timeInMillis
        return rootview
    }

    private inner class ItemClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            val item = v.getTag(R.id.tag_item_item) as String

        }
    }
}