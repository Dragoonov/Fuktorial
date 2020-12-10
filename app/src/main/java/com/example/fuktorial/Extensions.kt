package com.example.fuktorial

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.BaseColumns
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import com.example.fuktorial.database.FuktorialContract

fun <T : Fragment> FragmentActivity.replaceFragment(fragmentClass: Class<T>, args: Bundle? = null) {
    val tag = "Current Fragment"

    supportFragmentManager.commit {
        if (supportFragmentManager.backStackEntryCount == 0) {
            add(R.id.fragment_container, fragmentClass, args, tag)
        } else {
            replace(R.id.fragment_container, fragmentClass, args, tag)
        }
        addToBackStack(null)
    }
}

fun <T : Activity> Fragment.startFucktivity(fucktivityClass: Class<T>) {
    val intent = Intent(activity, fucktivityClass)
    activity?.startActivity(intent)
}

fun SQLiteDatabase.clearDatabase() {
    val sqlDeleteFuquotes = "DROP TABLE IF EXISTS ${FuktorialContract.Fuquotes.TABLE_NAME}"
    val sqlDeleteFucktivities =
        "DROP TABLE IF EXISTS ${FuktorialContract.Fucktivities.TABLE_NAME}"
    val sqlDeleteLastDIscovery = "DROP TABLE IF EXISTS ${FuktorialContract.LastDiscoveryTime.TABLE_NAME}"
    execSQL(sqlDeleteFucktivities)
    execSQL(sqlDeleteFuquotes)
    execSQL(sqlDeleteLastDIscovery)
}

fun SQLiteDatabase.createAndInitialize() {
    val sqlCreateFuquotes =
        "CREATE TABLE ${FuktorialContract.Fuquotes.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FuktorialContract.Fuquotes.COLUMN_NAME_TEXT} TEXT," +
                "${FuktorialContract.Fuquotes.COLUMN_NAME_DISCOVERED} INTEGER)"

    val sqlCreateFucktivities =
        "CREATE TABLE ${FuktorialContract.Fucktivities.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FuktorialContract.Fucktivities.COLUMN_NAME_NAME} TEXT," +
                "${FuktorialContract.Fucktivities.COLUMN_NAME_DISCOVERED} INTEGER," +
                "${FuktorialContract.Fucktivities.COLUMN_NAME_MASTERED} INTEGER)"

    val sqlCreateLastDiscovery =
        "CREATE TABLE ${FuktorialContract.LastDiscoveryTime.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FuktorialContract.LastDiscoveryTime.COLUMN_NAME_TIME} TEXT)"

    execSQL(sqlCreateFuquotes)
    execSQL(sqlCreateFucktivities)
    execSQL(sqlCreateLastDiscovery)
    FuquotesInfo.fuquotesList.forEach {
        insert(
            FuktorialContract.Fuquotes.TABLE_NAME,
            null,
            ContentValues().apply {
                put(FuktorialContract.Fuquotes.COLUMN_NAME_TEXT, it)
                put(FuktorialContract.Fuquotes.COLUMN_NAME_DISCOVERED, 0)
            })
    }
    FucktivitiesInfo.fucktivitiesList.forEach {
        insert(
            FuktorialContract.Fucktivities.TABLE_NAME,
            null,
            ContentValues().apply {
                put(
                    FuktorialContract.Fucktivities.COLUMN_NAME_NAME,
                    FucktivitiesInfo.getFucktivityName(it)
                )
                put(FuktorialContract.Fucktivities.COLUMN_NAME_DISCOVERED, 0)
                put(FuktorialContract.Fucktivities.COLUMN_NAME_MASTERED, 0)
            })
    }
    insert(
        FuktorialContract.LastDiscoveryTime.TABLE_NAME,
        null,
        ContentValues().apply {
            put(
                FuktorialContract.LastDiscoveryTime.COLUMN_NAME_TIME,
                "0"
            )
        })
}

fun SQLiteDatabase.resetProgress() {
    clearDatabase()
    createAndInitialize()
}

fun Fragment.getViewModel() = (activity as MainActivity).viewModel
