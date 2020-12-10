package com.example.fuktorial.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import com.example.fuktorial.database.models.Fucktivity
import com.example.fuktorial.database.models.Fuquote
import com.example.fuktorial.resetProgress
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.Executors

object RepositoryImpl : Repository {
    private var db: SQLiteDatabase? = null

    private val allFucktivities: BehaviorSubject<List<Fucktivity>> = BehaviorSubject.create()
    private val allFuquotes: BehaviorSubject<List<Fuquote>> = BehaviorSubject.create()
    private val lastDiscovery: BehaviorSubject<Date> = BehaviorSubject.create()

    private val executorService = Executors.newSingleThreadExecutor()

    override fun open(context: Context) {
        if (db == null || !(db as SQLiteDatabase).isOpen) {
            db = FuktorialDbHelper(context).writableDatabase
            executorService.execute {
                allFucktivities.onNext(fetchAllFucktivites())
                allFuquotes.onNext(fetchAllFuquotes())
                lastDiscovery.onNext(fetchLastDiscovery())
            }
        }
    }

    override fun close() {
        if(db != null && (db as SQLiteDatabase).isOpen) {
            db?.close()
        }
        if (!executorService.isShutdown) {
            executorService.shutdown()
        }
    }

    override fun insertFuquote(fuquote: Fuquote): Completable = Completable.fromAction {
            val values = ContentValues().apply {
                put(FuktorialContract.Fuquotes.COLUMN_NAME_TEXT, fuquote.text)
                put(FuktorialContract.Fuquotes.COLUMN_NAME_DISCOVERED, if(fuquote.discovered) 1 else 0)
            }
            if (db?.insert(FuktorialContract.Fuquotes.TABLE_NAME, null, values) == -1L) {
                throw RuntimeException("There was an error with inserting $values")
            } else {
                allFuquotes.onNext(fetchAllFuquotes())
            }
        }

    override fun updateFuquote(fuquote: Fuquote): Completable = Completable.fromAction {
        val values = ContentValues().apply {
            put(FuktorialContract.Fuquotes.COLUMN_NAME_DISCOVERED, if(fuquote.discovered) 1 else 0)
        }
        val selection = "${FuktorialContract.Fuquotes.COLUMN_NAME_TEXT} LIKE ?"
        if (db?.update(FuktorialContract.Fuquotes.TABLE_NAME, values, selection, arrayOf(fuquote.text)) == 0) {
            throw RuntimeException("There was an error with updating $values")
        } else {
            allFuquotes.onNext(fetchAllFuquotes())
        }
    }

    override fun updateFucktivity(fucktivity: Fucktivity): Completable = Completable.fromAction {
        val values = ContentValues().apply {
            put(FuktorialContract.Fucktivities.COLUMN_NAME_DISCOVERED, if (fucktivity.discovered) 1 else 0)
            put(FuktorialContract.Fucktivities.COLUMN_NAME_MASTERED, if (fucktivity.mastered) 1 else 0)
        }
        val selection = "${FuktorialContract.Fucktivities.COLUMN_NAME_NAME} LIKE ?"
        if (db?.update(FuktorialContract.Fucktivities.TABLE_NAME, values, selection, arrayOf(fucktivity.name)) == 0) {
            throw RuntimeException("There was an error with updating $values")
        } else {
            allFucktivities.onNext(fetchAllFucktivites())
        }
    }

    override fun insertFucktivity(fucktivity: Fucktivity): Completable = Completable.fromAction {
        val values = ContentValues().apply {
            put(FuktorialContract.Fucktivities.COLUMN_NAME_NAME, fucktivity.name)
            put(FuktorialContract.Fucktivities.COLUMN_NAME_DISCOVERED, if (fucktivity.discovered) 1 else 0)
            put(FuktorialContract.Fucktivities.COLUMN_NAME_MASTERED, if (fucktivity.mastered) 1 else 0)
        }
        if (db?.insert(FuktorialContract.Fucktivities.TABLE_NAME,null,  values) == -1L) {
            throw RuntimeException("There was an error with inserting $values")
        } else {
            allFucktivities.onNext(fetchAllFucktivites())
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

    override fun getUndicoveredFucktivities(): Observable<List<Fucktivity>> = allFucktivities
        .map { it.filter { fucktivity -> !fucktivity.discovered } }

    override fun getUndiscoveredFuquotes(): Observable<List<Fuquote>> = allFuquotes
        .map { it.filter { fuquote -> !fuquote.discovered } }

    override fun getLastDiscovery(): Observable<Date> = lastDiscovery

    private fun fetchLastDiscovery(): Date {
        val projection = arrayOf(
            FuktorialContract.LastDiscoveryTime.COLUMN_NAME_TIME)

        val cursor = db!!.query(
            FuktorialContract.LastDiscoveryTime.TABLE_NAME,   // The table to query
            projection,
            null,
            null,
            null,
            null,
            null
        )
        var time: Long
        with(cursor) {
            moveToNext()
            time = getString(getColumnIndexOrThrow(FuktorialContract.LastDiscoveryTime.COLUMN_NAME_TIME)).toLong()
        }
        cursor.close()
        return Date(time)
    }

    override fun updateLastDiscovery(date: Date): Completable = Completable.fromAction {
        val values = ContentValues().apply {
            put(FuktorialContract.LastDiscoveryTime.COLUMN_NAME_TIME, date.time.toString())
        }
        if (db?.update(FuktorialContract.LastDiscoveryTime.TABLE_NAME, values, null, null) == 0) {
            throw RuntimeException("There was an error with updating $values")
        } else {
            lastDiscovery.onNext(date)
        }
    }

    override fun getAllFuquotes(): Observable<List<Fuquote>> = allFuquotes

    private fun fetchAllFucktivites(): List<Fucktivity> {
        val projection = arrayOf(
            FuktorialContract.Fucktivities.COLUMN_NAME_NAME,
            FuktorialContract.Fucktivities.COLUMN_NAME_DISCOVERED,
            FuktorialContract.Fucktivities.COLUMN_NAME_MASTERED)

        val cursor = db!!.query(
            FuktorialContract.Fucktivities.TABLE_NAME,   // The table to query
            projection,
            null,
            null,
            null,
            null,
            null
        )
        val fucktivities = mutableListOf<Fucktivity>()
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(FuktorialContract.Fucktivities.COLUMN_NAME_NAME))
                val discovered = getInt(getColumnIndexOrThrow(FuktorialContract.Fucktivities.COLUMN_NAME_DISCOVERED))
                val mastered = getInt(getColumnIndexOrThrow(FuktorialContract.Fucktivities.COLUMN_NAME_MASTERED))
                fucktivities.add(
                    Fucktivity(name, discovered != 0, mastered != 0)
                )
            }
        }
        cursor.close()
        return fucktivities
    }

    private fun fetchAllFuquotes(): List<Fuquote> {
        val projection = arrayOf(
            FuktorialContract.Fuquotes.COLUMN_NAME_TEXT,
            FuktorialContract.Fuquotes.COLUMN_NAME_DISCOVERED)

        val cursor = db!!.query(
            FuktorialContract.Fuquotes.TABLE_NAME,   // The table to query
            projection,
            null,
            null,
            null,
            null,
            null
        )
        val fuquotes = mutableListOf<Fuquote>()
        with(cursor) {
            while (moveToNext()) {
                val name = getString(getColumnIndexOrThrow(FuktorialContract.Fuquotes.COLUMN_NAME_TEXT))
                val discovered = getInt(getColumnIndexOrThrow(FuktorialContract.Fuquotes.COLUMN_NAME_DISCOVERED))
                fuquotes.add(
                    Fuquote(name, discovered != 0)
                )
            }
        }
        cursor.close()
        return fuquotes
    }
}