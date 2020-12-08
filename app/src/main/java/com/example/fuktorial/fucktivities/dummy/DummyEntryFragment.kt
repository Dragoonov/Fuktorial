package com.example.fuktorial.fucktivities.dummy

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.fuktorial.R
import com.example.fuktorial.databinding.DummyEntryFragmentBinding
import com.example.fuktorial.fucktivities.tutorial.TutorialLevelFucktivity
import com.example.fuktorial.startFucktivity

class DummyEntryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DummyEntryFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@DummyEntryFragment
        }
        return binding.root
    }

    fun showBanner() =
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.congratulations)
            .setMessage(R.string.tutorialMessage1)
            .setPositiveButton(R.string.ok) { _: DialogInterface, _: Int ->
                startFucktivity(TutorialLevelFucktivity::class.java)
            }
            .create()
            .show()

}