package com.task.wikicats.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.task.wikicats.model.Cat
import com.task.wikicats.model.CatRepository
import com.task.wikicats.model.RetrofitClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed

class CatListViewModel(private val repository: CatRepository) : ViewModel() {

    val catListState: StateFlow<List<Cat>>
    private val _errorSharedFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorSharedFlow.asSharedFlow()

    init {
        catListState = flow {
            while (true) {
                val result = repository.getCatList()
                val catList = result.getOrNull()
                val error = result.exceptionOrNull()

                catList?.let { emit(it) }
                error?.let { _errorSharedFlow.emit(it) }

                delay(LIST_UPDATE_DELAY)
            }
        }.stateIn(viewModelScope, WhileSubscribed(STOP_UPDATE_FLOW_TIMEOUT), emptyList())
    }

    companion object {
        const val STOP_UPDATE_FLOW_TIMEOUT = 3000L
        const val LIST_UPDATE_DELAY = 10000L
    }


    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatListViewModel(CatRepository(RetrofitClient.apiService)) as T
        }
    }
}
