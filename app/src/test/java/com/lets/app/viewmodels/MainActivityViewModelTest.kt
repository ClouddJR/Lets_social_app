package com.lets.app.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
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
        viewModel.exploreButtonClicked()
        viewModel.exploreButtonClick.observeForever {
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

    @Test
    fun `should fire event after messages button click`() {
        viewModel.messagesButtonClick()
        viewModel.messagesButtonClick.observeForever {
            it.get()?.let {
                assertTrue(it)
            }
        }
    }


}