package com.example.fuktorial

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.preference.Preference
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.fuktorial.database.FuktorialContract
import com.example.fuktorial.notifications.NotificationWorker
import java.util.concurrent.TimeUnit

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

fun SQLiteDatabase.clearDatabase() {
    val sqlDeleteFuquotes = "DROP TABLE IF EXISTS ${FuktorialContract.Fuquotes.TABLE_NAME}"
    val sqlDeleteFucktivities =
        "DROP TABLE IF EXISTS ${FuktorialContract.Fucktivities.TABLE_NAME}"
    execSQL(sqlDeleteFucktivities)
    execSQL(sqlDeleteFuquotes)
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

    execSQL(sqlCreateFuquotes)
    execSQL(sqlCreateFucktivities)
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
                put(FuktorialContract.Fucktivities.COLUMN_NAME_NAME,
                    FucktivitiesInfo.getFucktivityName(it))
                put(FuktorialContract.Fucktivities.COLUMN_NAME_DISCOVERED, 0)
                put(FuktorialContract.Fucktivities.COLUMN_NAME_MASTERED, 0)
            })
    }
}

fun SQLiteDatabase.resetProgress() {
    clearDatabase()
    createAndInitialize()
}

fun Activity.getViewModel() = (this as MainActivity).viewModel

fun Fragment.getViewModel() = (activity as MainActivity).viewModel
