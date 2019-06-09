package com.example.lost.skillplus.models.managers

import com.example.lost.skillplus.models.podos.raw.*
import com.example.lost.skillplus.models.podos.responses.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BackendServiceManager {

    @GET("/users/all")
    fun getAllUsers(): Call<User>

    @POST("/users/signup")
    fun addUser(@Body user: User): Call<UserResponse>

    @POST("/users/login")
    fun loginUser(@Body user: User): Call<UserResponse>

    @GET(" /category/all")
    fun getCategories(): Call<CategoriesResponse>

    @POST(" /category")
    fun getCategoryPosts(@Body id: ActivatedCategory): Call<PostsResponse>

    @GET(" /category/name")
    fun getCategoryNames(): Call<List<String>>

    @POST(" /category/add/skill")
    fun addSkill(@Body skill: Skill): Call<SkillsResponse>

    @POST("/category/add/need")
    fun addNeed(@Body addNeed : AddNeed): Call<AddNeedResponse>

    @POST("/skill/apply")
    fun applySkill(@Body applySkill : ApplySkill) : Call<ApplySkillResponse>

    @GET(" /notifications/")
    fun getNotifications(@Query("user_id") user_id: Int, @Query("last_updated") lastUpdated: Long): Call<NotificationsResponse>
}