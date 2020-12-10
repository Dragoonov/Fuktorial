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
import java.util.*

class NoFucktivityFragment: Fragment() {

    private val handlerThread = HandlerThread("Updater")
    private val uiHandler = Handler(Looper.getMainLooper())


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val lastFucktivityDiscovery = getViewModel().lastFucktivityDiscovery.value!!
        val lastDiscovery = lastFucktivityDiscovery
        val binding = NoFucktivityFragmentBinding.inflate(layoutInflater, container, false)
        if (System.currentTimeMillis() - lastDiscovery.time > 1000 * 60 * 60 * 5) { // 5 hours passed
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
                    val time = System.currentTimeMillis() - lastDiscovery.time
                    if (time <= 1000 * 60 * 60 * 5) {
                        hours = 4 - time.div(1000*60*60).toInt()
                        minutes = 59 - (time.div(1000*60)%60).toInt()
                        seconds = 60 - (time.div(1000)%60).toInt()
                        if (previousSeconds != seconds) {
                            previousSeconds = seconds
                            uiHandler.post {
                                binding.text.text = getString(R.string.time_left, hours, minutes, seconds)
                            }
                        }
                    } else {
                        break
                    }
                }
            }
        }
        return binding.root
    }
}