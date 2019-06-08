package com.example.lost.skillplus.models.retrofit

import com.example.lost.skillplus.models.podos.raw.*
import com.example.lost.skillplus.models.podos.responses.*
import org.androidannotations.annotations.rest.Post
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

    @GET(" /category/add/skill")
    fun addSkill(@Body user: User): Call<Skill>

//TODO
// test it in request
    @POST("/category/add/need")
    fun addNeed(@Body addNeed : AddNeed): Call<AddNeedResponce>

    @POST("/skill/apply")
    fun applySkill(@Body applySkill : ApplySkill) : Call<ApplySkillResponse>
}