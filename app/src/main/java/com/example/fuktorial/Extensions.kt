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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.fuktorial.database.FuktorialContract

fun <T : Fragment> FragmentActivity.replaceFragment(fragmentClass: Class<T>, args: Bundle? = null) {
    val tag = "Current Fragment"
    if (supportFragmentManager.fragments.isNotEmpty() &&
        supportFragmentManager.fragments[0]::class.java == fragmentClass) {
        return
    }
    supportFragmentManager.commit {
        replace(R.id.fragment_container, fragmentClass, args, tag)
        addToBackStack(null)
    }
}

fun <T : Activity> Fragment.startFucktivity(fucktivityClass: Class<T>) {
    val intent = Intent(activity, fucktivityClass)
    activity?.startActivity(intent)
}

fun LiveData<Boolean>.observeUntilTrue(lifecycleOwner: LifecycleOwner, observer: Observer<Boolean>) {
    observe(lifecycleOwner, object : Observer<Boolean> {
        override fun onChanged(value: Boolean?) {
            observer.onChanged(value)
            if (value == true) {
                removeObserver(this)
            }
        }
    })
}

fun SQLiteDatabase.clearDatabase() {
    val sqlDeleteFuquotes = "DROP TABLE IF EXISTS ${FuktorialContract.Fuquotes.TABLE_NAME}"
    val sqlDeleteFucktivities =
        "DROP TABLE IF EXISTS ${FuktorialContract.Fucktivities.TABLE_NAME}"
    val sqlDeleteVars = "DROP TABLE IF EXISTS ${FuktorialContract.Vars.TABLE_NAME}"
    execSQL(sqlDeleteFucktivities)
    execSQL(sqlDeleteFuquotes)
    execSQL(sqlDeleteVars)
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
        "CREATE TABLE ${FuktorialContract.Vars.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FuktorialContract.Vars.COLUMN_NAME_TIME} TEXT," +
                "${FuktorialContract.Vars.COULMN_NAME_DISPLAYED_ENTRY} TEXT)"


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
        FuktorialContract.Vars.TABLE_NAME,
        null,
        ContentValues().apply {
            put(FuktorialContract.Vars.COLUMN_NAME_TIME, "0")
            put(FuktorialContract.Vars.COULMN_NAME_DISPLAYED_ENTRY, "")
        })
}

fun SQLiteDatabase.resetProgress() {
    clearDatabase()
    createAndInitialize()
}

fun Fragment.getViewModel() = (activity as MainActivity).viewModel
