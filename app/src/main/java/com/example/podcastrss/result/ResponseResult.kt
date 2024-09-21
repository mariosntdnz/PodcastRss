package com.example.podcastrss.result


sealed class ResponseResult<out T> {
    data object Loading: ResponseResult<Nothing>()
    data object Empty : ResponseResult<Nothing>()
    data class Error(val msg: String) : ResponseResult<Nothing>()
    data class Success<T>(val data:T) : ResponseResult<T>()
}