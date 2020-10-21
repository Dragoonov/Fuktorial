package com.example.fuktorial.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.fuktorial.database.models.Fucktivity
import com.example.fuktorial.database.models.Fuquote
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

class RepositoryImpl(private val context: Context): Repository {
    private var db: SQLiteDatabase? = null


    override fun open() {
        if (db == null) {
           db = FuktorialDbHelper(context).writableDatabase
        }
    }

    override fun close(): Unit = db?.close()!!


    override fun discoverQuote(): Completable {
        TODO("Not yet implemented")
    }

    override fun discoverFucktivity(): Completable {
        TODO("Not yet implemented")
    }

    override fun masterFucktivity(): Completable {
        TODO("Not yet implemented")
    }

    override fun resetProgress(): Completable {
        TODO("Not yet implemented")
    }

    override fun getDiscoveredFucktivities(): Observable<List<Fucktivity>> {
        TODO("Not yet implemented")
    }

    override fun getAllFucktivities(): Observable<List<Fucktivity>> {
        TODO("Not yet implemented")
    }

    override fun getDiscoveredFuquotes(): Observable<List<Fuquote>> {
        TODO("Not yet implemented")
    }

    override fun getAllFuquotes(): Observable<List<Fuquote>> {
        TODO("Not yet implemented")
    }
}