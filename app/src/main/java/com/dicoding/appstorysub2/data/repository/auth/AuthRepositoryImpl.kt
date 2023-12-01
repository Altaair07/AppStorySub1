package com.dicoding.appstorysub2.data.repository.auth

import com.dicoding.appstorysub2.data.local.sharedpref.SessionManager
import com.dicoding.appstorysub2.data.remote.RemoteDataSource
import com.dicoding.appstorysub2.data.remote.response.LoginResultResponse
import com.dicoding.appstorysub2.data.remote.response.RegisterResponse
import com.dicoding.appstorysub2.util.Result
import com.dicoding.appstorysub2.util.wrapEspressoIdlingResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val sessionManager: SessionManager,
) : AuthRepository {

    override fun login(email: String, password: String): Flow<Result<LoginResultResponse>> = flow {
        emit(Result.Loading())
        wrapEspressoIdlingResource {
            try {
                val result = remoteDataSource.login(email, password).loginResult
                sessionManager.apply {
                    saveAuthToken(result.token.toString())
                    saveLoginState(true)
                }
                emit(Result.Success(result))
            } catch (e: Exception) {
                emit(Result.Error(e.message))
            }
        }
    }.flowOn(Dispatchers.IO)

    override fun register(
        name: String,
        email: String,
        password: String,
    ): Flow<Result<RegisterResponse>> = flow {
        emit(Result.Loading())
        try {
            val result = remoteDataSource.register(name, email, password)
            emit(Result.Success(result))
        } catch (e: Exception) {
            emit(Result.Error(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override fun getLoginState(): Flow<Boolean> = flow {
        val state = sessionManager.getLoginState()
        emit(state)
    }.flowOn(Dispatchers.IO)

    override fun logout() {
        sessionManager.logout()
    }
}