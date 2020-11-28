package com.example.fuktorial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fuktorial.databinding.NoFucktivityFragmentBinding

class NoFucktivityFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val message = arguments?.getString(Constants.NO_FUCKTIVITY_KEY)
        val binding = NoFucktivityFragmentBinding.inflate(layoutInflater, container, false).apply {
            text.text = message
        }
        return binding.root
    }
}