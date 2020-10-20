package com.example.fuktorial

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit

fun<T : Fragment> FragmentActivity.replaceFragment(fragmentClass: Class<T>) {
    val tag = "Current Fragment"
    supportFragmentManager.commit {
        if (supportFragmentManager.backStackEntryCount == 0) {
            add(R.id.fragment_container, fragmentClass, null, tag)
        } else {
            replace(R.id.fragment_container, fragmentClass, null, tag)
        }
        addToBackStack(null)
    }
}

fun <T : Activity> Fragment.startFucktivity(fucktivityClass: Class<T>) {
    val intent = Intent(activity, fucktivityClass)
    activity?.startActivity(intent)
}
