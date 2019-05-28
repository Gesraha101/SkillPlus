package com.example.lost.skillplus.views.Retrofit

import com.example.lost.skillplus.views.models.User
import com.example.lost.skillplus.views.models.UserResponse
import com.example.lost.skillplus.views.models.logUser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

public interface Nodejs {


    @GET("/users/all")
    fun getAllUsers(): Call<User>


    @POST("/users/signup")
    fun addUser(@Body user: User): Call<UserResponse>

    @POST("/users/login")
    fun loginUser(@Body user: logUser): Call<UserResponse>
}