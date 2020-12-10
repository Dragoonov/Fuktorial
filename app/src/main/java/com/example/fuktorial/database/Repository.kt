package com.example.fuktorial.database

import android.content.Context
import com.example.fuktorial.database.models.Fucktivity
import com.example.fuktorial.database.models.Fuquote
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import java.util.*

interface Repository {
    fun open(context: Context)
    fun close()
    fun insertFuquote(fuquote: Fuquote): Completable
    fun updateFuquote(fuquote: Fuquote): Completable
    fun updateFucktivity(fucktivity: Fucktivity): Completable
    fun insertFucktivity(fucktivity: Fucktivity): Completable
    fun resetProgress(): Completable
    fun getDiscoveredFucktivities(): Observable<List<Fucktivity>>
    fun getMasteredFucktivities(): Observable<List<Fucktivity>>
    fun getAllFucktivities(): Observable<List<Fucktivity>>
    fun getDiscoveredFuquotes(): Observable<List<Fuquote>>
    fun getAllFuquotes(): Observable<List<Fuquote>>
    fun getUndicoveredFucktivities(): Observable<List<Fucktivity>>
    fun getUndiscoveredFuquotes(): Observable<List<Fuquote>>
    fun getLastDiscovery(): Observable<Date>
    fun updateLastDiscovery(date: Date): Completable
}