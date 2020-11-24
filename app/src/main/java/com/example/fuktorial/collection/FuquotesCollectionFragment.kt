package com.example.fuktorial.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fuktorial.databinding.FuquotesCollectionFragmentBinding
import com.example.fuktorial.getViewModel

class FuquotesCollectionFragment : Fragment() {

    private lateinit var binding: FuquotesCollectionFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewmodel = getViewModel()
        binding = FuquotesCollectionFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            with(recyclerView) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                adapter = FuquoteCollectionAdapter(listOf())
            }
        }
        viewmodel.discoveredFuquotes.observe(viewLifecycleOwner, Observer {
            (binding.recyclerView.adapter as FuquoteCollectionAdapter).updateDataSet(it)
        })
        return binding.root
    }
}