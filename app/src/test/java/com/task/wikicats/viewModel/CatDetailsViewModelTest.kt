package com.task.wikicats.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.task.wikicats.model.Cat
import com.task.wikicats.model.CatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
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
class CatDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var catDetailsViewModel: CatDetailsViewModel

    @Mock
    private lateinit var catRepository: CatRepository
    private lateinit var catPreview: Cat

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        catPreview = Cat(0, "Cat name", "Very fluffy cat", "thumbnail url", "image url")
        catDetailsViewModel = CatDetailsViewModel(catRepository, catPreview)
    }

    @Test
    fun catState_FistEqualsCatPreview_SecondEqualsRemoteCat() = runTest {
        val remoteCat = catPreview.copy(name = "Another Cat")
        Mockito.`when`(catRepository.getCat(catPreview.id))
            .thenReturn(Result.success(remoteCat))

        val resultList = catDetailsViewModel.catState.take(2).toList()

        delay(3000)
        assertThat(resultList).hasSize(2)
        assertThat(resultList[0]).isEqualTo(catPreview)
        assertThat(resultList[1]).isEqualTo(remoteCat)
    }

    @Test
    fun errorFlow_containsExceptionFromRepository() = runTest {
        val exception = Exception()
        Mockito.`when`(catRepository.getCat(catPreview.id))
            .thenReturn(Result.failure(exception))

        val actual = catDetailsViewModel.errorFlow.first()
        assertThat(actual).isEqualTo(exception)
    }
}