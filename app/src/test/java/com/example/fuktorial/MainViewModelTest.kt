package com.example.fuktorial

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.fuktorial.database.Repository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import java.util.*


class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repository: Repository = mockk {
        every { getDisplayedEntry() } returns Observable.just("")
        every { getDiscoveredFucktivities() } returns Observable.just(listOf())
        every { getMasteredFucktivities() } returns Observable.just(listOf())
        every { getDiscoveredFuquotes() } returns Observable.just(listOf())
        every { getUndicoveredFucktivities() } returns Observable.just(listOf())
        every { getLastDiscovery() } returns Observable.just(Date())
        every { open(any()) } just Runs
    }
    private val viewModel = MainViewModel(repository)

    @Before
    fun init() {
        viewModel.initialize(mockk(relaxed = true))
    }

    @Test
    fun `should enable notifications`() {

    }
}