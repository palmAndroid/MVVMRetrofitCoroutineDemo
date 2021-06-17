package com.mvvmretrofitcoroutinedemo.data.repository

import com.mvvmretrofitcoroutinedemo.data.api.ApiHelper

class MainRepository (private val apiHelper: ApiHelper) {

    suspend fun getUsers() = apiHelper.getUsers()
}