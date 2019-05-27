package com.example.lost.skillplus.models.interfaces

import com.example.lost.skillplus.models.podos.RequestsList
import retrofit2.Call
import retrofit2.http.GET

interface RequestsResponse {
    @get:GET("Deploy/")
    val Requests: Call<RequestsList>
}
