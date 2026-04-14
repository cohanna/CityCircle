package com.csci448.kate_lundberg.citycircle.util.api

import android.util.Log
import com.csci448.kate_lundberg.citycircle.data.Event
import com.csci448.kate_lundberg.citycircle.data.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class CityCircleFetchr {

    companion object {
        private const val LOG_TAG = "448.CityCircleFetchr"
    }

    private val cityCircleApiService: CityCircleApiService

    private val _eventsFlow = MutableSharedFlow<List<Event>?>(
        extraBufferCapacity = 1
    )
    val eventsFlow: SharedFlow<List<Event>?> = _eventsFlow.asSharedFlow()

    private val _usersFlow = MutableSharedFlow<List<User>?>(
        extraBufferCapacity = 1
    )
    val usersFlow: SharedFlow<List<User>?> = _usersFlow.asSharedFlow()

    init {
        val contentType = "application/json; charset=utf-8".toMediaType()

        val retrofit = Retrofit.Builder()
            .baseUrl(CityCircleApiService.BASE_API_URL)
            .addConverterFactory(Json{ ignoreUnknownKeys = true }.asConverterFactory(contentType))
            .build()

        cityCircleApiService = retrofit.create(CityCircleApiService::class.java)
    }

    private var _eventsRequest: Call<Map<String, Event>>? = null
    fun getEvents() {
        _eventsRequest?.cancel()
        _eventsRequest = cityCircleApiService.getEvents()
        _eventsRequest?.enqueue(object : Callback<Map<String, Event>> {
            override fun onFailure(call: Call<Map<String, Event>>, t: Throwable) {
                Log.e(LOG_TAG, "getEvents onFailure() called $t")
                _eventsFlow.tryEmit(null)
            }

            override fun onResponse(
                call: Call<Map<String, Event>>,
                response: Response<Map<String, Event>>
            ) {
                Log.d(LOG_TAG, "getEvents onResponse() called")
                val responseBody = response.body()
                if (responseBody != null) {
                    val eventList = responseBody.values.toList()
                    Log.d(LOG_TAG, "Events received: $eventList")
                    _eventsFlow.tryEmit(eventList)
                } else {
                    Log.d(LOG_TAG, "getEvents response body is null")
                    _eventsFlow.tryEmit(null)
                }
            }
        })
    }

    fun cancelEventsRequest() {
        _eventsRequest?.cancel()
        _eventsFlow.tryEmit(null)
    }

    private var _usersRequest: Call<Map<String, User>>? = null
    fun getUsers() {
        _usersRequest?.cancel()
        _usersRequest = cityCircleApiService.getUsers()
        _usersRequest?.enqueue(object : Callback<Map<String, User>> {
            override fun onFailure(call: Call<Map<String, User>>, t: Throwable) {
                Log.e(LOG_TAG, "getUsers onFailure() called $t")
                _usersFlow.tryEmit(null)
            }

            override fun onResponse(
                call: Call<Map<String, User>>,
                response: Response<Map<String, User>>
            ) {
                Log.d(LOG_TAG, "getUsers onResponse() called")
                val responseBody = response.body()
                if (responseBody != null) {
                    val userList = responseBody.values.toList()
                    Log.d(LOG_TAG, "Users received: $userList")
                    _usersFlow.tryEmit(userList)
                } else {
                    Log.d(LOG_TAG, "getUsers response body is null")
                    _usersFlow.tryEmit(null)
                }
            }
        })
    }

    fun cancelUsersRequest() {
        _usersRequest?.cancel()
        _usersFlow.tryEmit(null)
    }

    private var _putEventRequest: Call<Event>? = null
    fun putEvent(event: Event) {
        _putEventRequest?.cancel()
        _putEventRequest = cityCircleApiService.putEvent(event.id, event)
        _putEventRequest?.enqueue(object : Callback<Event> {
            override fun onFailure(call: Call<Event>, t: Throwable) {
                Log.e(LOG_TAG, "putEvent onFailure() called $t")
            }

            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                Log.d(LOG_TAG, "putEvent onResponse() called for event: ${event.id}")
            }
        })
    }

    private var _putUserRequest: Call<User>? = null
    fun putUser(user: User) {
        _putUserRequest?.cancel()
        _putUserRequest = cityCircleApiService.putUser(user.username, user)
        _putUserRequest?.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e(LOG_TAG, "putUser onFailure() called $t")
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                Log.d(LOG_TAG, "putUser onResponse() called for user: ${user.username}")
            }
        })
    }
}