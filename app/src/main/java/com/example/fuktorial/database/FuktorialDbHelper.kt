package com.example.fuktorial.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.fuktorial.FucktivitiesInfo
import com.example.fuktorial.FuquotesInfo

class FuktorialDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private val sqlCreateFuquotes =
        "CREATE TABLE ${FuktorialContract.Fuquotes.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FuktorialContract.Fuquotes.COLUMN_NAME_TEXT} TEXT," +
                "${FuktorialContract.Fuquotes.COLUMN_NAME_DISCOVERED} INTEGER)"

    private val sqlDeleteFuquotes = "DROP TABLE IF EXISTS ${FuktorialContract.Fuquotes.TABLE_NAME}"

    private val sqlCreateFucktivities =
        "CREATE TABLE ${FuktorialContract.Fucktivities.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FuktorialContract.Fucktivities.COLUMN_NAME_NAME} TEXT," +
                "${FuktorialContract.Fucktivities.COLUMN_NAME_DISCOVERED} INTEGER)," +
                "${FuktorialContract.Fucktivities.COLUMN_NAME_MASTERED} INTEGER)"

    private val sqlDeleteFucktivities =
        "DROP TABLE IF EXISTS ${FuktorialContract.Fucktivities.TABLE_NAME}"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(sqlCreateFuquotes)
        db.execSQL(sqlCreateFucktivities)
        FuquotesInfo.fuquotesList.forEach {
            db.insert(
                FuktorialContract.Fuquotes.TABLE_NAME,
                null,
                ContentValues().apply {
                    put(FuktorialContract.Fuquotes.COLUMN_NAME_TEXT, it)
                    put(FuktorialContract.Fuquotes.COLUMN_NAME_DISCOVERED, 0)
            })
        }
        FucktivitiesInfo.fucktivitiesList.forEach {
            db.insert(
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

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(sqlDeleteFucktivities)
        db.execSQL(sqlDeleteFuquotes)
        onCreate(db)
        //TODO Change that to insert/update new values before posting to Google Play
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Fuktorial.db"
    }
}