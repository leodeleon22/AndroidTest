package com.techyourchance.mockitofundamentals.example7

import com.techyourchance.mockitofundamentals.example7.authtoken.AuthTokenCache
import com.techyourchance.mockitofundamentals.example7.networking.LoginHttpEndpointSync.EndpointResult
import com.techyourchance.mockitofundamentals.example7.eventbus.EventBusPoster
import com.techyourchance.mockitofundamentals.example7.eventbus.LoggedInEvent
import com.techyourchance.mockitofundamentals.example7.networking.LoginHttpEndpointSync
import com.techyourchance.mockitofundamentals.example7.networking.NetworkErrorException
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginUseCaseSyncTest {

    @MockK
    private lateinit var mLoginHttpEndpointSyncMock: LoginHttpEndpointSync
    @MockK
    private lateinit var mAuthTokenCacheMock: AuthTokenCache
    @MockK
    private lateinit var mEventBusPosterMock: EventBusPoster

    private lateinit var SUT: LoginUseCaseSync

    @BeforeAll
    @Throws(Exception::class)
    fun setup() {
        SUT = LoginUseCaseSync(mLoginHttpEndpointSyncMock, mAuthTokenCacheMock, mEventBusPosterMock)
    }

    @AfterEach
    fun clear() {
        clearMocks(mLoginHttpEndpointSyncMock,mEventBusPosterMock)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_usernameAndPasswordPassedToEndpoint() {
        success()
        val slot= mutableListOf<String>()
        SUT.loginSync(USERNAME, PASSWORD)
        verify(exactly = 1) { mLoginHttpEndpointSyncMock.loginSync(capture(slot),capture(slot)) }
        assertEquals(USERNAME,slot[0])
        assertEquals(PASSWORD,slot[1])
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_authTokenCached() {
        success()
        val slot = CapturingSlot<String>()
        SUT.loginSync(USERNAME, PASSWORD)
        verify { mAuthTokenCacheMock.cacheAuthToken(capture(slot)) }
        assertEquals(AUTH_TOKEN, slot.captured)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_authTokenNotCached() {
        generalError()
        SUT.loginSync(USERNAME, PASSWORD)
        verify { mAuthTokenCacheMock wasNot Called }
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_authTokenNotCached() {
        authError()
        SUT.loginSync(USERNAME, PASSWORD)
        verify { mAuthTokenCacheMock wasNot Called }
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_authTokenNotCached() {
        serverError()
        SUT.loginSync(USERNAME, PASSWORD)
        verify { mAuthTokenCacheMock wasNot Called }
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_loggedInEventPosted() {
        success()
        val slot = CapturingSlot<Any>()
        SUT.loginSync(USERNAME, PASSWORD)
        verify { mEventBusPosterMock.postEvent(capture(slot)) }
        assertTrue(slot.captured is LoggedInEvent)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_noInteractionWithEventBusPoster() {
        generalError()
        SUT.loginSync(USERNAME, PASSWORD)
        verify { mEventBusPosterMock wasNot Called }
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_noInteractionWithEventBusPoster() {
        authError()
        SUT.loginSync(USERNAME, PASSWORD)
        verify { mEventBusPosterMock wasNot Called }
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_noInteractionWithEventBusPoster() {
        serverError()
        SUT.loginSync(USERNAME, PASSWORD)
        verify { mEventBusPosterMock wasNot Called }
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_successReturned() {
        success()
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(LoginUseCaseSync.UseCaseResult.SUCCESS, result)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_failureReturned() {
        serverError()
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_failureReturned() {
        authError()
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_failureReturned() {
        generalError()
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(LoginUseCaseSync.UseCaseResult.FAILURE, result)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_networkError_networkErrorReturned() {
        networkError()
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(LoginUseCaseSync.UseCaseResult.NETWORK_ERROR, result)
    }

    @Throws(Exception::class)
    private fun networkError() {
        every {
            mLoginHttpEndpointSyncMock.loginSync(any(),any())
        } throws NetworkErrorException()
    }

    @Throws(NetworkErrorException::class)
    private fun success() {
        every {
            mLoginHttpEndpointSyncMock.loginSync(any(),any())
        } returns EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SUCCESS, AUTH_TOKEN)
        every { mAuthTokenCacheMock.cacheAuthToken(any()) } just Runs
        every { mEventBusPosterMock.postEvent(any()) } just Runs
    }

    @Throws(Exception::class)
    private fun generalError() {
        every {
            mLoginHttpEndpointSyncMock.loginSync(any(),any())
        } returns EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.GENERAL_ERROR, "")
    }

    @Throws(Exception::class)
    private fun authError() {
        every {
            mLoginHttpEndpointSyncMock.loginSync(any(),any())
        } returns EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.AUTH_ERROR, "")
    }

    @Throws(Exception::class)
    private fun serverError() {
        every {
            mLoginHttpEndpointSyncMock.loginSync(any(),any())
        } returns EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SERVER_ERROR, "")
    }

    companion object {
        val USERNAME = "username"
        val PASSWORD = "password"
        val AUTH_TOKEN = "authToken"
    }

}