package com.dicoding.appstorysub2.data.local.sharedpref

interface SessionManager {
    fun saveAuthToken(token: String)
    fun fetchAuthToken(): String?
    fun saveLoginState(isLogin: Boolean)
    fun getLoginState(): Boolean
    fun logout()
}