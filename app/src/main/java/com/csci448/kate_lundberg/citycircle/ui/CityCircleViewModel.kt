package com.csci448.kate_lundberg.citycircle.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.csci448.kate_lundberg.citycircle.data.Event
import com.csci448.kate_lundberg.citycircle.data.User
import com.csci448.kate_lundberg.citycircle.util.api.CityCircleFetchr
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CityCircleViewModel : ViewModel() {

    companion object {
        private const val LOG_TAG = "448.CityCircleViewModel"
    }

    private val _cityCircleFetchr = CityCircleFetchr()

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()

    init {
        viewModelScope.launch {
            _cityCircleFetchr.eventsFlow.collect { result ->
                if (result != null) _events.update { result }
                Log.d(LOG_TAG, "Events from Firebase: $result")
            }
        }
        viewModelScope.launch {
            _cityCircleFetchr.usersFlow.collect { result ->
//                if (result != null) _users.update { result }
//                Log.d(LOG_TAG, "Users from Firebase: $result")
                if (result != null) {
                    _users.update { currentList ->
                        if (currentList.isEmpty()) {
                            result
                        } else {
                            result.map { incoming ->
                                val local = currentList.firstOrNull { it.username == incoming.username }
                                if (local?.profilePictureUrl != null && incoming.profilePictureUrl == null) {
                                    local
                                } else {
                                    incoming
                                }
                            } + currentList.filter { local ->
                                result.none { it.username == local.username }
                            }
                        }
                    }
                }
            }
        }
        _cityCircleFetchr.getEvents()
        _cityCircleFetchr.getUsers()
    }

    fun addEvent(event: Event) {
        _cityCircleFetchr.putEvent(event)
        _events.update { currentList -> currentList + event }
        val updatedUsers = _users.value.map { user ->
            if (user.username == event.createdBy) {
                user.copy(eventsHosted = user.eventsHosted + event)
            } else {
                user
            }
        }
        _users.update { updatedUsers }
        val updatedHost = updatedUsers.firstOrNull() { it.username == event.createdBy }
        if (updatedHost != null) {
            _cityCircleFetchr.putUser(updatedHost) {}
        }
    }

    fun addUser(user: User, onCompleted: () -> Unit = {}) {
        _users.update { currentList -> currentList + user }
       onCompleted()
        _cityCircleFetchr.putUser(user) {}
    }

    fun updateUser(user: User, onCompleted: () -> Unit = {}) {
        _cityCircleFetchr.putUser(user, onCompleted)
        _users.update { currentList ->
            currentList.map {
                if (it.username == user.username) user else it
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _cityCircleFetchr.cancelEventsRequest()
        _cityCircleFetchr.cancelUsersRequest()
    }

    fun rsvpEvent(eventId: String, username: String, isAttending: Boolean) {
        val updatedEvents = _events.value.map { event ->
            if (event.id == eventId) {
                val updatedAttendees = if (isAttending) {
                    event.attendees + username
                } else {
                    event.attendees - username
                }
                event.copy(attendees = updatedAttendees)
            } else event
        }
        _events.update { updatedEvents }
        val updatedEvent = updatedEvents.firstOrNull { it.id == eventId }
        if (updatedEvent != null) _cityCircleFetchr.putEvent(updatedEvent)

        val updatedUsers = _users.value.map { user ->
            if (user.username == username) {
                val event = updatedEvents.firstOrNull { it.id == eventId }
                if (event != null) {
                    val updatedAttended = if (isAttending) {
                        user.eventsAttended + event
                    } else {
                        user.eventsAttended.filter { it.id != eventId }
                    }
                    user.copy(eventsAttended = updatedAttended)
                } else user
            } else user
        }
        _users.update { updatedUsers }
        val updatedUser = updatedUsers.firstOrNull { it.username == username }
        if (updatedUser != null) _cityCircleFetchr.putUser(updatedUser) {}
    }
}