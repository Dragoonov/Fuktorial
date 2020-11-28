package com.example.fuktorial

import android.app.Activity
import androidx.fragment.app.Fragment
import com.example.fuktorial.fucktivities.tutorial.TutorialLevelFucktivity

object FucktivitiesInfo {
    val fucktivitiesList = listOf(
        TutorialLevelFucktivity::class.java
    )

    fun getFucktivityLevelClass(name: String) = fucktivitiesList.find {
        it.simpleName == name + "LevelFucktivity"
    }

    fun <T : Activity> getFucktivityName(fucktivityClass : Class<T>) = fucktivityClass
        .simpleName
        .removeSuffix("LevelFucktivity")
}