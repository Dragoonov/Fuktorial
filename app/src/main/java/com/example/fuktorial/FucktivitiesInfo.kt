package com.example.fuktorial

import com.example.fuktorial.fucktivities.dummy.DummyEntryFragment
import com.example.fuktorial.fucktivities.dummy.DummyLevelFucktivity
import com.example.fuktorial.fucktivities.tutorial.TutorialEntryFragment
import com.example.fuktorial.fucktivities.tutorial.TutorialLevelFucktivity

object FucktivitiesInfo {
    val fucktivitiesList = listOf(
        TutorialLevelFucktivity::class.java,
        DummyLevelFucktivity::class.java
    )

    val entriesList = listOf(
        TutorialEntryFragment::class.java,
        DummyEntryFragment::class.java,
        NoFucktivityFragment::class.java
    )

    fun getFucktivityLevelClass(name: String) = fucktivitiesList.find {
        it.simpleName == name + "LevelFucktivity"
    }

    fun <T : Any> getFucktivityName(fucktivityClass : Class<T>) = fucktivityClass
        .simpleName
        .removeSuffix("LevelFucktivity")
        .removeSuffix("EntryFragment")
        .removeSuffix("ViewModel")


    fun getEntryByName(fucktivityName: String) = entriesList.find {
        val name = fucktivityName
            .removeSuffix("LevelFucktivity")
            .removeSuffix("EntryFragment")
            .removeSuffix("ViewModel")
        it.name.contains(name)
    } ?: NoFucktivityFragment::class.java

}