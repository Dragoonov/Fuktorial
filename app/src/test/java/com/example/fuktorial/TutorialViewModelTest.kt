package com.example.fuktorial

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.fuktorial.database.Repository
import com.example.fuktorial.fucktivities.tutorial.TutorialViewModel
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TutorialViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repository: Repository = mockk {
        every { updateFucktivity(any()) } returns Completable.complete()
        every { open(any()) } just Runs
    }
    private val viewModel = TutorialViewModel(repository)

    @Before
    fun init() {
        viewModel.initialize(mockk(relaxed = true))
    }

    @Test
    fun `should master fucktivity`() {
        //GIVEN
        val name = "name"
        //WHEN
        viewModel.masterFucktivity(name)
        //THEN
        verify {
            repository.updateFucktivity(match { it.name == name })
        }
    }

    @Test
    fun `should launch after clicks`() {
        //GIVEN WHEN
        repeat(15) {
            viewModel.onClick()
        }

        verify(exactly = 1) {
            repository.updateFucktivity( match { it.name == "Tutorial" })
        }
    }

}