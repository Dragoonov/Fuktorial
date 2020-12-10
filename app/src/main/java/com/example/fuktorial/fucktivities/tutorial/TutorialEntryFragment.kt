package com.example.fuktorial.fucktivities.tutorial

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.fuktorial.FucktivitiesInfo
import com.example.fuktorial.R
import com.example.fuktorial.databinding.FragmentTutorialEntryBinding
import com.example.fuktorial.getViewModel
import com.example.fuktorial.startFucktivity
import io.reactivex.rxjava3.disposables.CompositeDisposable


class TutorialEntryFragment : Fragment() {

    private val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
            disposable.add(
                getViewModel().discoverFucktivity(FucktivitiesInfo.getFucktivityName(this::class.java), requireContext()).subscribe {
                    showBanner()
                }
            )
        } else {
            next.visibility = View.VISIBLE
        }
    }

    private fun showBanner() =
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.congratulations)
            .setMessage(R.string.tutorialMessage1)
            .setPositiveButton(R.string.ok) { _: DialogInterface, _: Int ->
                startFucktivity(TutorialLevelFucktivity::class.java)
            }
            .create()
            .show()

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }

}
