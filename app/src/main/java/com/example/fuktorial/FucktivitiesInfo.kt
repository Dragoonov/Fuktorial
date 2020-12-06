package com.example.fuktorial

import android.app.Activity
import androidx.fragment.app.Fragment
import com.example.fuktorial.fucktivities.tutorial.TutorialEntryFragment
import com.example.fuktorial.fucktivities.tutorial.TutorialLevelFucktivity

object FucktivitiesInfo {
    val fucktivitiesList = listOf(
        TutorialLevelFucktivity::class.java
    )

    val entriesList = listOf(
        TutorialEntryFragment::class.java
    )

    fun getFucktivityLevelClass(name: String) = fucktivitiesList.find {
        it.simpleName == name + "LevelFucktivity"
    }

    fun <T : Any> getFucktivityName(fucktivityClass : Class<T>) = fucktivityClass
        .simpleName
        .removeSuffix(if (fucktivityClass::class.java.isAssignableFrom(Activity::class.java) ) "LevelFucktivity" else "EntryFragment")


    fun getEntryByName(fucktivityName: String) = entriesList.find {
        it.name.contains(fucktivityName)
    }

}