package com.task.wikicats.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.task.wikicats.model.Cat
import com.task.wikicats.model.CatRepository
import com.task.wikicats.model.RetrofitClient
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CatDetailsViewModel(private val repository: CatRepository, catPreview: Cat) : ViewModel() {

    private val _catState = MutableStateFlow(catPreview)
    val catState: StateFlow<Cat> = _catState.asStateFlow()
    private val _errorSharedFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorSharedFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            val result = repository.getCat(catPreview.id)
            val cat = result.getOrNull()
            val error = result.exceptionOrNull()

            cat?.let { _catState.value = it }
            error?.let { _errorSharedFlow.emit(it) }
        }
    }

    class Factory(private val catPreview: Cat) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CatDetailsViewModel(CatRepository(RetrofitClient.apiService), catPreview) as T
        }
    }
}
