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
        val lastFucktivityDiscovery = getViewModel().lastFucktivityDiscovery.value!!
        val binding = NoFucktivityFragmentBinding.inflate(layoutInflater, container, false)
        if (getViewModel().undiscoveredFucktivities.value!!.isEmpty()) { // 5 hours passed
            binding.text.text = getString(R.string.all_complete)
        } else {
            var seconds = 0
            var minutes: Int
            var hours: Int
            var previousSeconds = seconds
            handlerThread.start()
            val handler = Handler(handlerThread.looper)
            handler.post {
                while (true) {
                    val time = System.currentTimeMillis() - lastFucktivityDiscovery.time
                    if (time <= Constants.WAITING_TIME) {
                        hours = 4 - time.div(1000 * 60 * 60).toInt()
                        minutes = 59 - (time.div(1000 * 60) % 60).toInt()
                        seconds = 59 - (time.div(1000) % 60).toInt()
                        if (previousSeconds != seconds) {
                            previousSeconds = seconds
                            uiHandler.post {
                                binding.text.text = getString(R.string.time_left, hours, minutes, seconds)
                            }
                        }
                    } else if (time > Constants.WAITING_TIME + 1000) {
                        disposable.add(getViewModel().resetDisplayedEntry().subscribe {
                            (activity as MainActivity).replaceFragment(getViewModel().findAppropriateFragment()!!)
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