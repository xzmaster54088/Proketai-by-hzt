package com.pocketdev.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubAuthManager @Inject constructor(
    private val context: Context
) {
    
    companion object {
        private const val CLIENT_ID = "YOUR_GITHUB_CLIENT_ID" // Replace with your GitHub OAuth App Client ID
        private const val CLIENT_SECRET = "YOUR_GITHUB_CLIENT_SECRET" // Replace with your GitHub OAuth App Client Secret
        private const val REDIRECT_URI = "pocketdev://oauth"
        private const val SCOPE = "repo,user"
    }
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.NotAuthenticated)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    fun generateAuthUrl(): String {
        return "https://github.com/login/oauth/authorize" +
                "?client_id=$CLIENT_ID" +
                "&redirect_uri=$REDIRECT_URI" +
                "&scope=$SCOPE" +
                "&state=${System.currentTimeMillis()}"
    }
    
    fun launchAuthFlow() {
        val authUrl = generateAuthUrl()
        val customTabsIntent = CustomTabsIntent.Builder().build()
        customTabsIntent.launchUrl(context, Uri.parse(authUrl))
    }
    
    suspend fun handleOAuthCallback(uri: Uri): Boolean {
        val code = uri.getQueryParameter("code")
        val error = uri.getQueryParameter("error")
        
        return if (code != null) {
            // Exchange code for access token
            val token = exchangeCodeForToken(code)
            if (token != null) {
                _authState.value = AuthState.Authenticated(token)
                true
            } else {
                false
            }
        } else if (error != null) {
            _authState.value = AuthState.Error(error)
            false
        } else {
            false
        }
    }
    
    private suspend fun exchangeCodeForToken(code: String): String? {
        // TODO: Implement token exchange with GitHub API
        // This requires making a POST request to https://github.com/login/oauth/access_token
        // with client_id, client_secret, code, and redirect_uri
        return null // Placeholder
    }
    
    fun logout() {
        _authState.value = AuthState.NotAuthenticated
    }
    
    sealed class AuthState {
        object NotAuthenticated : AuthState()
        data class Authenticated(val accessToken: String) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}
