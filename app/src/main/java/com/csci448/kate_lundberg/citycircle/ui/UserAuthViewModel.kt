package com.csci448.kate_lundberg.citycircle.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.csci448.kate_lundberg.citycircle.util.api.CityCircleFetchr
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserAuthViewModel : ViewModel() {

    companion object {
        private const val LOG_TAG = "448.AuthViewModel"
    }

    private val auth: FirebaseAuth = Firebase.auth
    private val _cityCircleFetchr = CityCircleFetchr()
    private val _currentUser = MutableStateFlow<FirebaseUser?>(auth.currentUser)
    private val _currentUsername = MutableStateFlow("")
    val currentUser: StateFlow<FirebaseUser?> = _currentUser.asStateFlow()
    val currentUsername: String get() = _currentUsername.value

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "signInWithEmail:success")
                    _currentUser.value = auth.currentUser
                    _cityCircleFetchr.getUserByEmail(email) { user ->
                        _currentUsername.value = user?.username ?: email.substringBefore("@")
                        onSuccess()
                    }
                } else {
                    Log.w(LOG_TAG, "signInWithEmail:failure", task.exception)
                    onError(task.exception?.message ?: "Authentication failed")
                }
            }
    }

    fun createAccount(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(LOG_TAG, "createUserWithEmail:success")
                    _currentUser.value = auth.currentUser
                    onSuccess()
                } else {
                    Log.w(LOG_TAG, "createUserWithEmail:failure", task.exception)
                    onError(task.exception?.message ?: "Account creation failed")
                }
            }
    }

    fun setCurrentUsername(username: String) {
        _currentUsername.value = username
    }

    fun setCurrentBio(){

    }

    fun signOut() {
        auth.signOut()
        _currentUser.value = null
    }
}