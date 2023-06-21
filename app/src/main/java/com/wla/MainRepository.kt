package com.wla

import com.wla.util.RemoteSource
import com.wla.api.Api

class MainRepository {

    private val api: Api = Provider.provideApi(
        apiUrl = Provider.provideApiBaseUrl(),
        okHttpClient = Provider.provideHttpClient(),
        gson = Provider.provideGson()
    )

    suspend fun search(q: String) = RemoteSource.launchResultFlow { api.searchObjectIds(q = q) }
}