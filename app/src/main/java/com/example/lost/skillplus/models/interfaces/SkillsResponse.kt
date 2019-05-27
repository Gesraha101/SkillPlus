package com.example.lost.skillplus.models.interfaces

import com.example.lost.skillplus.models.podos.SkillsList
import retrofit2.Call
import retrofit2.http.GET

interface SkillsResponse {
    @get:GET("Deploy/")
    val skills: Call<SkillsList>
}
