package com.example.lost.skillx.models.interfaces

import com.example.lost.skillx.models.podos.CategoriesList
import retrofit2.Call
import retrofit2.http.GET

interface CategoriesResponse {
    @get:GET("Deploy/")
    val categories: Call<CategoriesList>
}
