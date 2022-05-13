package com.task.wikicats.model

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CatRepository(private val apiService: ApiService) {

    suspend fun getCatList(): Result<List<Cat>> {
        return try {
            val catList = withContext(Dispatchers.IO) { apiService.getCatList() }.toCatList()
            Result.success(catList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCat(id: Long): Result<Cat> {
        return try {
            val catList = withContext(Dispatchers.IO) { apiService.getCat(id) }.toCatList()
            val cat = catList.first()
            Result.success(cat)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}