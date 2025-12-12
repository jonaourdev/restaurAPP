package com.example.restaurapp.model.network

object SessionManager{
    var token: String? = null
        private set

    fun saveToken(newToken: String){
        token = newToken
    }

    fun clearToken(){
        token = null

    }
}