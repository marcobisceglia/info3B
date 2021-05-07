package com.example.divingapp2021

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.divingapp2021.databinding.FragmentBoatBinding
import java.util.*

class BoatFragment : NavigationFragment<FragmentBoatBinding>() {
    private val adapter by lazy { BoatAdapter(listener) }
    private val listener = ItemClickListener()

    override fun buildBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): FragmentBoatBinding {
        return FragmentBoatBinding.inflate(inflater, container, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootview = super.onCreateView(inflater, container, savedInstanceState).apply {
            isClickable = true
            isFocusable = true
        }

        setTitle("List of boat")
        showBackButton(true)

        this.binding.statusCBStatisticsRecyclerView.adapter = adapter


        return rootview
    }


    override fun onResume() {
        super.onResume()
        updateLayout()
    }


    private fun updateLayout() {
        val items = ArrayList<Boat>()
        items.add(object : Boat() {
            init {
                name = "Boat 1"
                model = "Model 1"
                places = "10"
            }
        })
        items.add(object : Boat() {
            init {
                name = "Boat 2"
                model = "Model 1"
                places = "10"
            }
        })
        items.add(object : Boat() {
            init {
                name = "Boat 3"
                model = "Model 1"
                places = "10"
            }
        })
        items.add(object : Boat() {
            init {
                name = "Boat 4"
                model = "Model 1"
                places = "10"
            }
        })

        adapter.submitList(items)
    }

    private inner class ItemClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            val item = v.getTag(R.id.tag_item_item) as Boat
            FragmentHelper.addFragmentFromSide(
                    requireActivity(),
                    EditBoatFragment(item),
                    R.id.mainFrameLayout
            )
        }
    }
}
