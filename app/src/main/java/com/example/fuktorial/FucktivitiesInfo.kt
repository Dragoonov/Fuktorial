package com.example.fuktorial

import android.app.Activity
import com.example.fuktorial.fucktivities.tutorial.TutorialLevelActivity

object FucktivitiesInfo {
    private val fucktivitiesList = listOf(
        TutorialLevelActivity::class.java
    )

    fun getFucktivityLevelClass(name: String) = fucktivitiesList.find {
        it.simpleName == name + "LevelActivity"
    }

    fun <T : Activity> getFucktivityName(fucktivityClass : Class<T>) = fucktivityClass
        .simpleName
        .removeSuffix("LevelActivity")
}