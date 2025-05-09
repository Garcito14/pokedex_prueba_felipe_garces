package com.example.prueba_tecnica_felipe_garces_pokedex_2025_abril.ui.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest

import com.example.prueba_tecnica_felipe_garces_pokedex_abril_2025.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.security.MessageDigest
import java.util.UUID

class AuthenticationManager(val context: Context) {

    private val auth = Firebase.auth


    private  fun createNonce():String{
         val rawNonce = UUID.randomUUID().toString()
         val bytes = rawNonce.toByteArray()
         val md = MessageDigest.getInstance("SHA-256")
         val digest = md.digest(bytes)
         return digest.fold("") {str,it ->
             str + "%02x".format(it)
         }
     }

    fun signInWithGoogle(): Flow<AuthResponse> = callbackFlow {
        val googleIdOptions = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.web_client_id))
            .setAutoSelectEnabled(false)
            .setNonce(createNonce())
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOptions)
            .build()

        try {
            val credentialManager = CredentialManager.create(context)
            val result = credentialManager.getCredential(context = context, request = request)
            val credential = result.credential
            if (credential is CustomCredential) {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        val fireBaseCredential =
                            GoogleAuthProvider.getCredential(
                                googleIdTokenCredential.idToken,
                                null
                            )
                        auth.signInWithCredential(fireBaseCredential)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    trySend(AuthResponse.Success)

                                } else {
                                    trySend(
                                        AuthResponse.Error(
                                            message = it.exception?.message ?: ""
                                        )
                                    )
                                }
                            }
                    } catch (e: GoogleIdTokenParsingException) {
                        trySend(AuthResponse.Error(message = e.message ?: ""))
                    }
                }
            }
        } catch (e: Exception) {
            trySend(AuthResponse.Error(message = e.message ?: ""))
        }
        awaitClose()
    }
    }
    interface  AuthResponse {

        data object Success: AuthResponse
        data class Error(val message:String ):AuthResponse
    }
