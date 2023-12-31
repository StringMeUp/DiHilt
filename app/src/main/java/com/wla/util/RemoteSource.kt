package com.wla.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import timber.log.Timber

object RemoteSource {

    sealed class Resource<out T> {
        object Loading : Resource<Nothing>()
        data class Success<out T>(val data: T?) : Resource<T>()
        data class Error(val responseCode: Int? = null) : Resource<Nothing>()
    }

    private const val MM_DEBUG_TAG = "MM_DEBUG_TAG"
    fun <T> launchResultFlow(apiResponse: suspend () -> Response<T>): Flow<Resource<T>> =
        flow {
            emit(Resource.Loading)
            val response = apiResponse.invoke()
            response.let {
                if (response.isSuccessful) {
                    emit(Resource.Success(it.body()!!))
                } else {
                    emit(Resource.Error(response.code()))
                    Timber.d("$MM_DEBUG_TAG Http request failed errorBody -> ${response.errorBody()}")
                }
            }
        }.catch { httpErr ->
            emit(Resource.Error())
            Timber.d("$MM_DEBUG_TAG Unknown error occurred -> $httpErr")
        }.flowOn(Dispatchers.IO)
}