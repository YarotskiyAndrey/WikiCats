package com.task.wikicats.model

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("api.php?action=query&generator=categorymembers&gcmtitle=Category:Natural_cat_breeds&prop=pageimages|extracts&exintro&explaintext&piprop=thumbnail|original&format=json")
    suspend fun getCatList(): WikiResponseModel

    @GET("api.php?action=query&prop=pageimages|extracts&piprop=thumbnail|original&format=json")
    suspend fun getCat(@Query("pageids") pageId: Long): WikiResponseModel

}