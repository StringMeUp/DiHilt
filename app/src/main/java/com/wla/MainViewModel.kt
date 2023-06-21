package com.wla

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wla.models.ArtItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow

class MainViewModel() : ViewModel() {

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

//    fun searchIds(q: String): Flow<MutableList<ArtItem>> {
//        return callbackFlow {
//            searchJob = vi.launch {
//                repository.search(q)
//                    .collect {
//                        when (it) {
//                            is Resource.Loading -> {
//                                trySend(ArtItem.loading(true))
//                            }
//                            is Resource.Success -> {
//                                val items = it.data?.artIds?.map { ArtItem(it, ObjectType.ART) }
//                                    ?.toMutableList()
//                                trySend(items ?: ArtItem.empty())
//                            }
//                            is Resource.Error -> {
//                                trySend(ArtItem.error())
//                            }
//                        }
//                    }
//            }
//
//            awaitClose { searchJob?.cancel(CancellationException("${Constants.DEBUG_INTENDED} Search cancelled.")) }
//        }
//    }
}