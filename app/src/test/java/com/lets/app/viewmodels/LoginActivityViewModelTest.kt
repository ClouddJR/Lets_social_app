package com.lets.app.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lets.app.repositories.UserRepository
import junit.framework.Assert.assertNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class LoginActivityViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel

    @Mock
    private lateinit var userRepository: UserRepository


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = LoginViewModel(userRepository)
    }

    @Test
    fun `should fire event after login success`() {
        viewModel.loginWasSuccessful()

        viewModel.loginSucceed.observeForever {
            it?.get()?.let {
                assertTrue(it)
            }
        }
    }

    @Test
    fun `should fire error after login failed`() {
        viewModel.loginFailed()

        viewModel.loginError.observeForever {
            it?.get()?.let {
                assertTrue(it)
            }
        }
    }

    @Test
    fun `should navigate when user has already logged in`() {
        `when`(userRepository.isUserLoggedIn()).thenReturn(true)

        viewModel.init()
        viewModel.userAlreadyLoggedIn.observeForever {
            it?.get()?.let {
                assertTrue(it)
            }
        }
    }

    @Test
    fun `should stay in login activity when user has not already logged in`() {
        `when`(userRepository.isUserLoggedIn()).thenReturn(false)

        viewModel.init()
        assertNull(viewModel.userAlreadyLoggedIn.value)
    }
}