package com.csci448.kate_lundberg.citycircle.util.api

import com.csci448.kate_lundberg.citycircle.data.Event
import com.csci448.kate_lundberg.citycircle.data.User
import retrofit2.Call
import retrofit2.http.*

interface CityCircleApiService {
    companion object {
        const val BASE_API_URL = "https://citycircle-37f1a-default-rtdb.firebaseio.com/"
    }

    @GET("events.json")
    fun getEvents(): Call<Map<String, Event>>

    @GET("events/{id}.json")
    fun getEvent(@Path("id") eventId: String): Call<Event>

    @PUT("events/{id}.json")
    fun putEvent(@Path("id") eventId: String, @Body event: Event): Call<Event>

    @GET("users.json")
    fun getUsers(): Call<Map<String, User>>

    @GET("users/{username}.json")
    fun getUser(@Path("username") username: String): Call<User>

    @PUT("users/{username}.json")
    fun putUser(@Path("username") username: String, @Body user: User): Call<User>

    @PATCH("users/{username}.json")
    fun patchUser(@Path("username") username: String, @Body fields: Map<String, String>): Call<Map<String, String>>
}