package com.lets.app.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.Assert.assertNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MainActivityViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainActivityViewModel

    @Before
    fun setup() {
        viewModel = MainActivityViewModel()
    }

    @Test
    fun `should fire event after home button click`() {
        viewModel.homeButtonClicked()
        viewModel.homeButtonClick.observeForever {
            it.get()?.let {
                assertTrue(it)
            }
        }
    }

    @Test
    fun `should be null before home button click`() {
        viewModel.homeButtonClick.observeForever {
            assertNull(it.get())
        }
    }

    @Test
    fun `should fire event after map button click`() {
        viewModel.mapButtonClicked()
        viewModel.mapButtonClick.observeForever {
            it.get()?.let {
                assertTrue(it)
            }
        }
    }

    @Test
    fun `should fire event after profile button click`() {
        viewModel.profileButtonClicked()
        viewModel.profileButtonClick.observeForever {
            it.get()?.let {
                assertTrue(it)
            }
        }
    }

}