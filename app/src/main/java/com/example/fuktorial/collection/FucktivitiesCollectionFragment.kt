package com.example.fuktorial.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fuktorial.databinding.FucktivitiesCollectionFragmentBinding
import com.example.fuktorial.getViewModel

class FucktivitiesCollectionFragment : Fragment() {

    private lateinit var binding: FucktivitiesCollectionFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewmodel = getViewModel()
        binding = FucktivitiesCollectionFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            recyclerView.apply {
                adapter = FucktivityCollectionAdapter(listOf())
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        }
        viewmodel.discoveredFucktivities.observe(viewLifecycleOwner, Observer {
            (binding.recyclerView.adapter as FucktivityCollectionAdapter).updateDataSet(it)
        })
        return binding.root
    }
}