package com.example.fuktorial.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.fuktorial.database.models.Fucktivity
import com.example.fuktorial.database.models.Fuquote
import com.example.fuktorial.resetProgress
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import java.lang.RuntimeException

class RepositoryImpl(private val context: Context) : Repository {
    private var db: SQLiteDatabase? = null

    private val allFucktivities: PublishSubject<List<Fucktivity>> = PublishSubject.create()
    private val allFuquotes: PublishSubject<List<Fuquote>> = PublishSubject.create()

    override fun open() {
        if (db == null) {
            db = FuktorialDbHelper(context).writableDatabase
        }
    }

    override fun close(): Unit = db?.close()!!

    override fun insertFuquote(fuquote: Fuquote): Completable = Completable.fromAction {
            val values = ContentValues().apply {
                put(FuktorialContract.Fuquotes.COLUMN_NAME_TEXT, fuquote.text)
                put(FuktorialContract.Fuquotes.COLUMN_NAME_DISCOVERED, fuquote.discovered)
            }
            if (db?.insert(FuktorialContract.Fuquotes.TABLE_NAME, null, values) == -1L) {
                throw RuntimeException("There was an error with inserting $values")
            }
        }

    override fun updateFuquote(fuquote: Fuquote): Completable = Completable.fromAction {
        val values = ContentValues().apply {
            put(FuktorialContract.Fuquotes.COLUMN_NAME_DISCOVERED, fuquote.discovered)
        }
        val selection = "${FuktorialContract.Fuquotes.TABLE_NAME} LIKE ?"
        if (db?.update(FuktorialContract.Fuquotes.TABLE_NAME, values, selection, arrayOf(fuquote.text)) == 0) {
            throw RuntimeException("There was an error with updating $values")
        }
    }

    override fun updateFucktivity(fucktivity: Fucktivity): Completable = Completable.fromAction {
        val values = ContentValues().apply {
            put(FuktorialContract.Fucktivities.COLUMN_NAME_DISCOVERED, fucktivity.discovered)
            put(FuktorialContract.Fucktivities.COLUMN_NAME_MASTERED, fucktivity.mastered)
        }
        val selection = "${FuktorialContract.Fucktivities.TABLE_NAME} LIKE ?"
        if (db?.update(FuktorialContract.Fucktivities.TABLE_NAME, values, selection, arrayOf(fucktivity.name)) == 0) {
            throw RuntimeException("There was an error with updating $values")
        }
    }

    override fun insertFucktivity(fucktivity: Fucktivity): Completable = Completable.fromAction {
        val values = ContentValues().apply {
            put(FuktorialContract.Fucktivities.COLUMN_NAME_NAME, fucktivity.name)
            put(FuktorialContract.Fucktivities.COLUMN_NAME_DISCOVERED, fucktivity.discovered)
            put(FuktorialContract.Fucktivities.COLUMN_NAME_MASTERED, fucktivity.mastered)
        }
        if (db?.insert(FuktorialContract.Fucktivities.TABLE_NAME,null,  values) == -1L) {
            throw RuntimeException("There was an error with inserting $values")
        }
    }

    override fun resetProgress(): Completable = Completable.fromAction {
        db?.resetProgress()
    }

    override fun getDiscoveredFucktivities(): Observable<List<Fucktivity>> = allFucktivities
        .map { it.filter { fucktivity -> fucktivity.discovered } }


    override fun getMasteredFucktivities(): Observable<List<Fucktivity>> = allFucktivities
        .map { it.filter { fucktivity -> fucktivity.mastered } }

    override fun getAllFucktivities(): Observable<List<Fucktivity>> = allFucktivities

    override fun getDiscoveredFuquotes(): Observable<List<Fuquote>> = allFuquotes
        .map { it.filter { fuquote -> fuquote.discovered } }

    override fun getAllFuquotes(): Observable<List<Fuquote>> = allFuquotes
}