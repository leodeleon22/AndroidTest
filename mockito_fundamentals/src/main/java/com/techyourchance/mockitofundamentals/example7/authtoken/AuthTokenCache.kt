package com.techyourchance.mockitofundamentals.example7.authtoken

interface AuthTokenCache {

    val authToken: String

    fun cacheAuthToken(authToken: String)
}
