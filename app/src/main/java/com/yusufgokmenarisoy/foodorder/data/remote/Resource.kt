package com.yusufgokmenarisoy.foodorder.data.remote

data class Resource<out T> (val status: Status, val message: String? = null, val data: T? = null) {

    enum class Status {
        LOADING,
        SUCCESS,
        ERROR
    }

    companion object {
        fun <T> loading(): Resource<T> = Resource(Status.LOADING)

        fun <T> success(data: T): Resource<T> = Resource(Status.SUCCESS, data = data)

        fun <T> error(message: String): Resource<T> = Resource(Status.ERROR, message)
    }
}