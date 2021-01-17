package com.example.fuktorial

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.fuktorial.database.Repository
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkObject
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.After
import org.junit.Test
import org.junit.Before
import org.junit.Rule
import java.util.Date


class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val notificationsObserver = Observer<Boolean> {}

    private val repository: Repository = mockk {
        every { getDisplayedEntry() } returns Observable.just("mock")
        every { getDiscoveredFucktivities() } returns Observable.just(listOf())
        every { getMasteredFucktivities() } returns Observable.just(listOf())
        every { getDiscoveredFuquotes() } returns Observable.just(listOf())
        every { getUndicoveredFucktivities() } returns Observable.just(listOf())
        every { getLastDiscovery() } returns Observable.just(Date())
        every { updateLastDiscovery(any()) } returns Completable.complete()
        every { updateDisplayedEntry(any()) } returns Completable.complete()
        every { updateFucktivity(any()) } returns Completable.complete()
        every { open(any()) } just Runs
    }
    private val viewModel = MainViewModel(repository)

    @Before
    fun init() {
        viewModel.initialize(mockk(relaxed = true))
        viewModel.notificationsEnabled.observeForever(notificationsObserver)
    }

    @After
    fun cleanUp() {
        viewModel.notificationsEnabled.removeObserver(notificationsObserver)
        unmockkObject(FucktivitiesInfo)
    }

    @Test
    fun `should enable notifications`() {
        //GIVEN WHEN
        viewModel.enableNotifications(true)
        //THEN
        assertTrue(viewModel.notificationsEnabled.value!!)
        //WHEN
        viewModel.enableNotifications(false)
        assertFalse(viewModel.notificationsEnabled.value!!)
    }

    @Test
    fun `should find appropriate fragment`() {
        //GIVEN //WHEN
        val fragment = viewModel.refreshNextFragment()
        //THEN
        assertEquals(NoFucktivityFragment::class.java, fragment)

        verify {
            repository.updateDisplayedEntry(any())
        }
    }

    @Test
    fun `should discover fucktivity`() {
        //GIVEN
        val name = "mockName"
        //WHEN
        viewModel.discoverFucktivity(name)
        //THEN
        verify {
            repository.updateLastDiscovery(any())
            repository.updateDisplayedEntry("")
            repository.updateFucktivity(match { it.name == name })
        }
    }

    @Test
    fun `should calculate time left`() {
        //GIVEN
        val timePassed = 1000L
        val startingTime = 1500L
        //WHEN
        val calculated = viewModel.calculateTimeLeft(timePassed, startingTime)
        val calculated2 = viewModel.calculateTimeLeft(timePassed, startingTime)
        //THEN
        assertEquals(calculated, calculated2)
    }

}