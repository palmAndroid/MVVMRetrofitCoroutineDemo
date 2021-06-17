package com.mvvmretrofitcoroutinedemo.data.api

import com.mvvmretrofitcoroutinedemo.data.model.User
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>
}