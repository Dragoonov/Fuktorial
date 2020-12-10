package com.example.fuktorial.fucktivities.dummy

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.fuktorial.FucktivitiesInfo
import com.example.fuktorial.R
import com.example.fuktorial.databinding.DummyEntryFragmentBinding
import com.example.fuktorial.getViewModel
import com.example.fuktorial.startFucktivity
import io.reactivex.rxjava3.disposables.CompositeDisposable

class DummyEntryFragment : Fragment() {

    private val disposable = CompositeDisposable()

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

    fun handleClick() = disposable.add(getViewModel()
        .discoverFucktivity(FucktivitiesInfo.getFucktivityName(this::class.java), requireContext())
        .subscribe { showBanner() }
    )

    fun showBanner() =
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.congratulations)
            .setMessage(R.string.tutorialMessage1)
            .setPositiveButton(R.string.ok) { _: DialogInterface, _: Int ->
                startFucktivity(DummyLevelFucktivity::class.java)
            }
            .create()
            .show()

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }
}