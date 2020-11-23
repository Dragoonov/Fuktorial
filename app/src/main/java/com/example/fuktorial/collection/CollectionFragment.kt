package com.example.fuktorial.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.fuktorial.R
import com.example.fuktorial.databinding.CollectionFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class CollectionFragment : Fragment() {
    private lateinit var binding: CollectionFragmentBinding
    private lateinit var collectionAdapter: CollectionAdapter
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CollectionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        collectionAdapter = CollectionAdapter(this)
        viewPager = binding.pager
        viewPager.adapter = collectionAdapter
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = getString(
                if (position == 0) R.string.fucktivities
                else R.string.fuquotes
            )
        }.attach()
    }
}

