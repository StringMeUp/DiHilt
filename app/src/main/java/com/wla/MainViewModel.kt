package com.wla

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sr.metmuseum.util.Constants
import com.wla.models.ArtItem
import com.wla.util.RemoteSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val repository = MainRepository()

    enum class ObjectType {
        ART, ERROR, EMPTY, DEFAULT, LOADING
    }

    private var savedQuery: String? = null
    private var searchJob: Job? = null
    private var savedFlow: Flow<MutableList<ArtItem>>? = null

    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private var _itemId = MutableLiveData<Int?>()
    val itemId: LiveData<Int?> = _itemId

    fun setItemId(id: Int) {
        _itemId.value = id
    }

    fun saveQuery(q: String) {
        savedQuery = q
    }

    fun getQuery(): String? {
        return savedQuery
    }

    fun searchIds(q: String): Flow<MutableList<ArtItem>> {
        return callbackFlow {
            searchJob = viewModelScope.launch {
                repository.search(q)
                    .collect {
                        when (it) {
                            is RemoteSource.Resource.Loading -> {
                                trySend(ArtItem.loading(true))
                            }
                            is RemoteSource.Resource.Success -> {
                                val items = it.data?.artIds?.map { ArtItem(it, ObjectType.ART) }
                                    ?.toMutableList()
                                trySend(items ?: ArtItem.empty())
                            }
                            is RemoteSource.Resource.Error -> {
                                trySend(ArtItem.error())
                            }
                        }
                    }
            }

            awaitClose { searchJob?.cancel(CancellationException("${Constants.DEBUG_INTENDED} Search cancelled.")) }
        }
    }
}