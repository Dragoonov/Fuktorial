package com.example.fuktorial.database

import com.example.fuktorial.database.models.Fucktivity
import com.example.fuktorial.database.models.Fuquote
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

interface Repository {
    fun open()
    fun close()
    fun discoverQuote(): Completable
    fun discoverFucktivity(): Completable
    fun masterFucktivity(): Completable
    fun resetProgress(): Completable
    fun getDiscoveredFucktivities(): Observable<List<Fucktivity>>
    fun getAllFucktivities(): Observable<List<Fucktivity>>
    fun getDiscoveredFuquotes(): Observable<List<Fuquote>>
    fun getAllFuquotes(): Observable<List<Fuquote>>
}