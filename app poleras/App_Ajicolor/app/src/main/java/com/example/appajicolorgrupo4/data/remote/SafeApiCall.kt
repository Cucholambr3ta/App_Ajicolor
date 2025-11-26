package com.example.appajicolorgrupo4.data.remote

import retrofit2.Response
import java.io.IOException

abstract class SafeApiCall {
    suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Result<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(IOException("Response body is null"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody.isNullOrEmpty()) {
                    "Error code: ${response.code()}"
                } else {
                    // Try to parse JSON error if possible, otherwise return raw string (could be HTML from Vercel)
                    errorBody
                }
                Result.failure(IOException(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
