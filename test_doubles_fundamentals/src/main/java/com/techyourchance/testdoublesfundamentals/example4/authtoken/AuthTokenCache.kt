package com.techyourchance.testdoublesfundamentals.example4.authtoken

interface AuthTokenCache {

    var authToken: String

    fun cacheAuthToken(authToken: String)
}
