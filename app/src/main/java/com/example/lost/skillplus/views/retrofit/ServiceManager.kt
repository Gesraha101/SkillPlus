package com.example.lost.skillplus.views.retrofit

import com.example.lost.skillplus.models.podos.User
import com.example.lost.skillplus.models.podos.UserResponse
import com.example.lost.skillplus.models.podos.logUser
import retrofit2.Call
import retrofit2.http.*

public interface ServiceManager {


    @GET("/users/all")
    fun getAllUsers(): Call<User>


    @POST("/users/signup")
    fun addUser(@Body user: User): Call<UserResponse>

    @POST("/users/login")
    fun loginUser(@Body user: logUser): Call<UserResponse>
}