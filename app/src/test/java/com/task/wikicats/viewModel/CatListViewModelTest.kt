package com.task.wikicats.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.task.wikicats.model.Cat
import com.task.wikicats.model.CatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CatListViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var catListViewModel: CatListViewModel

    @Mock
    private lateinit var catRepository: CatRepository

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    companion object {
        private var catCount = 0L
    }

    private fun mockCat(): Cat {
        return Cat(catCount++, "mock name", "mock description", "mock link", "mock image")
    }


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        catListViewModel = CatListViewModel(catRepository)
    }

    @Test
    fun catListUpdatesWithDelay() = runTest {
        Mockito.`when`(catRepository.getCatList())
            .thenReturn(Result.success(List(10) { mockCat() }))

        val updatesAmount = 5
        val totalTime = CatListViewModel.LIST_UPDATE_DELAY * updatesAmount

        val statesList = ArrayList<List<Cat>>()
        catListViewModel.catListState
        val job = launch {
            catListViewModel.catListState
                .onEach {
                    //generate new cats
                    Mockito.`when`(catRepository.getCatList())
                        .thenReturn(Result.success(List(10) { mockCat() }))
                }
                .toList(statesList)
        }

        delay(totalTime)
        assertThat(statesList).hasSize(updatesAmount + 1)//plus 1 initial empty state
        job.cancel()
    }

    @Test
    fun errorFlow_containsExceptionFromRepository() = runTest {
        val exception = Exception()
        Mockito.`when`(catRepository.getCatList())
            .thenReturn(Result.failure(exception))

        val job = launch {
            //start cold flow
            catListViewModel.catListState.collect()
        }
        val actual = catListViewModel.errorFlow.first()
        assertThat(actual).isEqualTo(exception)
        job.cancel()
    }
}