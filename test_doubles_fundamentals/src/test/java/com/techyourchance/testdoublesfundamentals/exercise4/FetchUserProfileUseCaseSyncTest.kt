package com.techyourchance.testdoublesfundamentals.exercise4

import com.techyourchance.testdoublesfundamentals.example4.networking.NetworkErrorException
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync
import com.techyourchance.testdoublesfundamentals.exercise4.FetchUserProfileUseCaseSync.UseCaseResult
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync.EndpointResult
import com.techyourchance.testdoublesfundamentals.exercise4.networking.UserProfileHttpEndpointSync.EndpointResultStatus
import com.techyourchance.testdoublesfundamentals.exercise4.users.User
import com.techyourchance.testdoublesfundamentals.exercise4.users.UsersCache
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FetchUserProfileUseCaseSyncTest {

    private companion object {
        const val USER_ID = "id"
        const val USER_NAME = "name"
        const val USER_URL = "url"
    }

    private val endpoint= UserProfileHttpEndpointSyncTd()
    private val cache= UsersCacheTd()
    private val SUT = FetchUserProfileUseCaseSync(endpoint,cache)


    @AfterEach
    fun cleanup(){
        endpoint.mIsNetworkError = false
        endpoint.mStatus= null
        cache.mUserCache.clear()
    }

    @Test
    @Throws(Exception::class)
    fun `Given user id When get user Then pass user id to endpoint`() {
        SUT.fetchUserProfileSync(USER_ID)
        assertEquals(USER_ID, endpoint.mUserId)
    }

    @Test
    @Throws(Exception::class)
    fun `Given user id When get profile Then cache user`() {
        SUT.fetchUserProfileSync(USER_ID)
        val user = cache.getUser(USER_ID)
        assertEquals(USER_ID,user?.userId)
        assertEquals(USER_NAME,user?.fullName)
        assertEquals(USER_URL,user?.imageUrl)
    }

    @Test
    @Throws(Exception::class)
    fun `Given general error When get profile Then dont cache`() {
        endpoint.mStatus = EndpointResultStatus.GENERAL_ERROR
        SUT.fetchUserProfileSync(USER_ID)
        assertNull(cache.getUser(USER_ID))
    }

    @Test
    @Throws(Exception::class)
    fun `Given auth error When get profile Then dont cache`() {
        endpoint.mStatus = EndpointResultStatus.AUTH_ERROR
        SUT.fetchUserProfileSync(USER_ID)
        assertNull(cache.getUser(USER_ID))
    }

    @Test
    @Throws(Exception::class)
    fun `Given server error When get profile Then dont cache`() {
        endpoint.mStatus = EndpointResultStatus.SERVER_ERROR
        SUT.fetchUserProfileSync(USER_ID)
        assertNull(cache.getUser(USER_ID))
    }

    @Test
    @Throws(Exception::class)
    fun `Given user id When get profile Then return success`() {
        val result = SUT.fetchUserProfileSync(USER_ID)
        assertEquals(UseCaseResult.SUCCESS, result)
    }

    @Test
    @Throws(Exception::class)
    fun `Given user id When server error Then return failure`() {
        endpoint.mStatus = EndpointResultStatus.SERVER_ERROR
        val result = SUT.fetchUserProfileSync(USER_ID)
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    @Throws(Exception::class)
    fun `Given auth error When get profile Then return failure`() {
        endpoint.mStatus = EndpointResultStatus.AUTH_ERROR
        val result = SUT.fetchUserProfileSync(USER_ID)
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    @Throws(Exception::class)
    fun `Given general error When get profile Then return failure`() {
        endpoint.mStatus = EndpointResultStatus.GENERAL_ERROR
        val result = SUT.fetchUserProfileSync(USER_ID)
        assertEquals(UseCaseResult.FAILURE, result)
    }

    @Test
    @Throws(Exception::class)
    fun `Given network error When get profile Then return error`() {
        endpoint.mIsNetworkError = true
        val result = SUT.fetchUserProfileSync(USER_ID)
        assertEquals(UseCaseResult.NETWORK_ERROR, result)
    }

    @Test
    @Throws(Exception::class)
    fun `Given auth error When get profile Then dont cache user`() {
        endpoint.mStatus = EndpointResultStatus.AUTH_ERROR
        SUT.fetchUserProfileSync(USER_ID)
        assertNull(cache.getUser(USER_ID))
    }

    private class UserProfileHttpEndpointSyncTd: UserProfileHttpEndpointSync {
        var mUserId = ""
        var mStatus: EndpointResultStatus? = null
        var mIsNetworkError = false

        @Throws(NetworkErrorException::class)
        override fun getUserProfile(userId: String): EndpointResult {
            mUserId = userId

            if(mIsNetworkError){
                throw NetworkErrorException()
            }
            return when(mStatus){
                EndpointResultStatus.AUTH_ERROR,
                EndpointResultStatus.SERVER_ERROR,
                EndpointResultStatus.GENERAL_ERROR
                -> EndpointResult(mStatus!!,"", "", "")
                else -> EndpointResult(EndpointResultStatus.SUCCESS,mUserId, USER_NAME, USER_URL)
            }
        }
    }

    private class UsersCacheTd: UsersCache {
        var mUserCache = mutableListOf<User>()
        override fun cacheUser(user: User) {
            val currentUser = getUser(user.userId)
            currentUser?.let { mUserCache.remove(it) }
            mUserCache.add(user)
        }

        override fun getUser(userId: String): User? {
            return mUserCache.find { it.userId == userId }
        }
    }
}