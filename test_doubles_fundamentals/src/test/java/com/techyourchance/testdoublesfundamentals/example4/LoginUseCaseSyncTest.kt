package com.techyourchance.testdoublesfundamentals.example4

import com.techyourchance.testdoublesfundamentals.example4.LoginUseCaseSync.UseCaseResult
import com.techyourchance.testdoublesfundamentals.example4.authtoken.AuthTokenCache
import com.techyourchance.testdoublesfundamentals.example4.eventbus.EventBusPoster
import com.techyourchance.testdoublesfundamentals.example4.eventbus.LoggedInEvent
import com.techyourchance.testdoublesfundamentals.example4.networking.LoginHttpEndpointSync
import com.techyourchance.testdoublesfundamentals.example4.networking.LoginHttpEndpointSync.EndpointResultStatus
import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class LoginUseCaseSyncTest {

    private val mLoginHttpEndpointSyncTd = LoginHttpEndpointSyncTd()
    private val mAuthTokenCacheTd = AuthTokenCacheTd()
    private val mEventBusPosterTd = EventBusPosterTd()

    private lateinit var SUT: LoginUseCaseSync

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        SUT = LoginUseCaseSync(mLoginHttpEndpointSyncTd, mAuthTokenCacheTd, mEventBusPosterTd)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_usernameAndPasswordPassedToEndpoint() {
        SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(USERNAME,mLoginHttpEndpointSyncTd.mUsername)
        assertEquals(PASSWORD, mLoginHttpEndpointSyncTd.mPassword)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_authTokenCached() {
        SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(AUTH_TOKEN, mAuthTokenCacheTd.authToken)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mStatus = EndpointResultStatus.GENERAL_ERROR
        SUT.loginSync(USERNAME, PASSWORD)
        assertTrue(mAuthTokenCacheTd.authToken.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mStatus = EndpointResultStatus.AUTH_ERROR
        SUT.loginSync(USERNAME, PASSWORD)
        assertTrue(mAuthTokenCacheTd.authToken.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_authTokenNotCached() {
        mLoginHttpEndpointSyncTd.mStatus = EndpointResultStatus.SERVER_ERROR
        SUT.loginSync(USERNAME, PASSWORD)
        assertTrue(mAuthTokenCacheTd.authToken.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_loggedInEventPosted() {
        SUT.loginSync(USERNAME, PASSWORD)
        assertTrue(mEventBusPosterTd.mEvent is LoggedInEvent)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mStatus = EndpointResultStatus.GENERAL_ERROR
        SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(0, mEventBusPosterTd.mInteractionsCount)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mStatus = EndpointResultStatus.AUTH_ERROR
        SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(0, mEventBusPosterTd.mInteractionsCount)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_noInteractionWithEventBusPoster() {
        mLoginHttpEndpointSyncTd.mStatus = EndpointResultStatus.SERVER_ERROR
        SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(0, mEventBusPosterTd.mInteractionsCount)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_success_successReturned() {
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(UseCaseResult.SUCCESS, result)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_serverError_failureReturned() {
        mLoginHttpEndpointSyncTd.mStatus = EndpointResultStatus.SERVER_ERROR
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_authError_failureReturned() {
        mLoginHttpEndpointSyncTd.mStatus = EndpointResultStatus.AUTH_ERROR
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_generalError_failureReturned() {
        mLoginHttpEndpointSyncTd.mStatus = EndpointResultStatus.GENERAL_ERROR
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    @Throws(Exception::class)
    fun loginSync_networkError_networkErrorReturned() {
        mLoginHttpEndpointSyncTd.mIsNetworkError = true
        val result = SUT.loginSync(USERNAME, PASSWORD)
        assertEquals(UseCaseResult.NETWORK_ERROR, result)
    }

    // ---------------------------------------------------------------------------------------------
    // Helper classes

    private class LoginHttpEndpointSyncTd : LoginHttpEndpointSync {
        var mUsername = ""
        var mPassword = ""
        var mStatus: EndpointResultStatus? = null
        var mIsNetworkError = false
        @Throws(NetworkErrorException::class)
        override fun loginSync(username: String, password: String): LoginHttpEndpointSync.EndpointResult {
            mUsername = username
            mPassword = password
            if(mIsNetworkError){
                throw NetworkErrorException()
            }
            return when(mStatus){
                EndpointResultStatus.GENERAL_ERROR,
                EndpointResultStatus.AUTH_ERROR,
                EndpointResultStatus.SERVER_ERROR ->
                    LoginHttpEndpointSync.EndpointResult(mStatus!!, "")
                else -> LoginHttpEndpointSync.EndpointResult(LoginHttpEndpointSync.EndpointResultStatus.SUCCESS, AUTH_TOKEN)
            }
        }
    }

    private class AuthTokenCacheTd : AuthTokenCache {

        override var authToken = ""

        override fun cacheAuthToken(authToken: String) {
            this.authToken = authToken
        }
    }

    private class EventBusPosterTd : EventBusPoster {
        var mEvent: Any = Unit
        var mInteractionsCount: Int = 0

        override fun postEvent(event: Any) {
            mInteractionsCount++
            mEvent = event
        }
    }

    companion object {

        val USERNAME = "username"
        val PASSWORD = "password"
        val AUTH_TOKEN = "authToken"
    }
}