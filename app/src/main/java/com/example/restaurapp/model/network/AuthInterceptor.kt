package com.example.restaurapp.model.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val token = SessionManager.token

        val newRequest = originalRequest.newBuilder()


        if (!token.isNullOrEmpty()) {
            newRequest.header("Authorization", "Bearer $token")
            Log.d("AuthInterceptor", "Enviando token en la petición")
        } else {
            Log.d("AuthInterceptor", "No se encontró el token")
        }

        return chain.proceed(newRequest.build())
    }
}