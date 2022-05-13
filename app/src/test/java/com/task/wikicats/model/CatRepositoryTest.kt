package com.task.wikicats.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class CatRepositoryTest {

    private lateinit var catRepository: CatRepository

    @Mock
    private lateinit var apiService: ApiService

    private val catListModel = WikiResponseModel(WikiQuery(mapOfCatPages(10)))
    private val catModel = WikiResponseModel(WikiQuery(mapOfCatPages(1)))

    private fun mapOfCatPages(pagesAmount: Int): Map<Long, WikiPage> {
        return HashMap<Long, WikiPage>().apply {
            putAll(List(pagesAmount) { page -> page.toLong() to mockCatPage(page) })
        }
    }

    private fun mockCatPage(page: Int): WikiPage {
        return WikiPage(
            page.toLong(),
            "title",
            WikiImage("source"),
            WikiImage("source"),
            "extract"
        )
    }

    @get:Rule
    val instantTaskExecutionRule: InstantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        catRepository = CatRepository(apiService)
    }

    @Test
    fun getCatList_ReceivesWikiResponseModel_ReturnsCatList() {
        runBlocking {
            Mockito.`when`(apiService.getCatList()).thenReturn(catListModel)
            val result = catRepository.getCatList().getOrNull()
            val expected =
                List(catListModel.query.pages.size) { mockCatPage(it) }.map { it.toCat() }
            assertThat(result).isEqualTo(expected)
        }
    }

    @Test
    fun getCat_ReceivesWikiResponseModel_ReturnsCat() {
        runBlocking {
            Mockito.`when`(apiService.getCat(0)).thenReturn(catModel)
            val result = catRepository.getCat(0).getOrNull()
            val expected = mockCatPage(0).toCat()
            assertThat(result).isEqualTo(expected)
        }
    }
}