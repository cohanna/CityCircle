package com.csci448.kate_lundberg.citycircle.utils

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.UUID
import kotlin.coroutines.resume

object FirebaseStorageUtil {

    private val storageReference = FirebaseStorage.getInstance().reference
    suspend fun uploadEventImage(imageUri: Uri): String? {
        return suspendCancellableCoroutine { continuation ->
            val ref = storageReference.child("event_images/${UUID.randomUUID()}")
            ref.putFile(imageUri)
                .addOnSuccessListener {
                    ref.downloadUrl
                        .addOnSuccessListener { uri ->
                            continuation.resume(uri.toString())
                        }
                        .addOnFailureListener {
                            continuation.resume(null)
                        }
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }
    }
}