package com.example.fuktorial.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fuktorial.databinding.FucktivitiesCollectionFragmentBinding

class FucktivitiesCollectionFragment : Fragment() {

    private lateinit var binding: FucktivitiesCollectionFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FucktivitiesCollectionFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

}