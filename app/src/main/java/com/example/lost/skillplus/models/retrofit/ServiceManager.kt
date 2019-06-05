package com.example.lost.skillplus.models.retrofit

import com.example.lost.skillplus.models.podos.raw.Category
import com.example.lost.skillplus.models.podos.raw.Skill
import com.example.lost.skillplus.models.podos.raw.User
import com.example.lost.skillplus.models.podos.responses.CategoriesResponse
import com.example.lost.skillplus.models.podos.responses.PostsResponse
import com.example.lost.skillplus.models.podos.responses.SkillsResponse
import com.example.lost.skillplus.models.podos.responses.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ServiceManager {

    @GET("/users/all")
    fun getAllUsers(): Call<User>

    @POST("/users/signup")
    fun addUser(@Body user: User): Call<UserResponse>

    @POST("/users/login")
    fun loginUser(@Body user: User): Call<UserResponse>

    @GET(" /category/all")
    fun getCategories(): Call<CategoriesResponse>

    @GET(" /category")
    fun getCategoryPosts(@Query("id") id: Int): Call<PostsResponse>

    @GET(" /category/name")
    fun getCategoryNames(): Call<List<String>>

    @POST(" /category/add/skill")
    fun addSkill(@Body skill: Skill): Call<SkillsResponse>
}