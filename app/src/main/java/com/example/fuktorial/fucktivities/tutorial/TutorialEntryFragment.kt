package com.example.fuktorial.fucktivities.tutorial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fuktorial.databinding.FragmentTutorialEntryBinding
import com.example.fuktorial.startFucktivity

class TutorialEntryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTutorialEntryBinding.inflate(inflater, container, false).apply {
            fragment = this@TutorialEntryFragment
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }
    @JvmOverloads
    fun handleClick(view: View, next: View? = null) {
        view.visibility = View.GONE
        if (next == null) {
            showBanner()
        } else {
            next.visibility = View.VISIBLE
        }
    }

    private fun showBanner() = startFucktivity(TutorialLevelActivity::class.java)

}
