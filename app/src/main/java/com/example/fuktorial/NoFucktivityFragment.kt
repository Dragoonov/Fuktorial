package com.example.fuktorial

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fuktorial.databinding.NoFucktivityFragmentBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

class NoFucktivityFragment : Fragment() {

    private val handlerThread = HandlerThread("Updater")
    private val uiHandler = Handler(Looper.getMainLooper())
    private val disposable = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = NoFucktivityFragmentBinding.inflate(layoutInflater, container, false)
        if (getViewModel().undiscoveredFucktivities.value!!.isEmpty()) {
            binding.text.text = getString(R.string.all_complete)
        } else {
            handlerThread.start()
            val handler = Handler(handlerThread.looper)
            val zeroTime = Triple(0,0,0)
            var previousSeconds = 0
            handler.post {
                while (true) {
                    val timeLeft = getViewModel().calculateTimeLeft(getViewModel().lastFucktivityDiscovery.value!!.time)
                    if (timeLeft != zeroTime) {
                        if (previousSeconds != timeLeft.third) {
                            previousSeconds = timeLeft.third
                            uiHandler.post {
                                binding.text.text = getString(R.string.time_left, timeLeft.first, timeLeft.second, timeLeft.third)
                            }
                        }
                    } else {
                        disposable.add(getViewModel().resetDisplayedEntry().subscribe {
                            (activity as MainActivity).replaceFragment(getViewModel().refreshNextFragment())
                        })
                        break
                    }
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        disposable.dispose()
        super.onDestroyView()
    }
}