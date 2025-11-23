package com.pocketdev.data.remote.interceptor

import com.pocketdev.data.repository.UserSettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DynamicHostInterceptor @Inject constructor(
    private val userSettings: UserSettingsRepository
) : Interceptor {
    
    // Cache configuration to avoid blocking IO in interceptor
    private var cachedConfig: com.pocketdev.domain.model.LlmConfig? = null
    
    init {
        // Listen for configuration changes and update cache
        CoroutineScope(Dispatchers.IO).launch {
            userSettings.configFlow.collect { config ->
                cachedConfig = config
            }
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val config = cachedConfig ?: runBlocking { 
            userSettings.configFlow.collect { } // Force collection to populate cache
            cachedConfig 
        } ?: return chain.proceed(request) // Fallback if no config

        // Skip if config is invalid
        if (!config.isValid()) {
            return chain.proceed(request)
        }

        try {
            // Parse the base URL
            val baseUrl = HttpUrl.parse(config.baseUrl) ?: return chain.proceed(request)
            
            // 1. Replace Base URL (Host, Port, Scheme)
            val newUrl = request.url.newBuilder()
                .scheme(baseUrl.scheme)
                .host(baseUrl.host)
                .port(baseUrl.port)
                .build()

            val requestBuilder = request.newBuilder()
                .url(newUrl)

            // 2. Dynamically inject Authorization header if API key is present
            if (config.apiKey.isNotBlank()) {
                requestBuilder.header("Authorization", "Bearer ${config.apiKey}")
            }

            request = requestBuilder.build()
            
            return chain.proceed(request)
        } catch (e: Exception) {
            // If URL parsing fails, proceed with original request
            return chain.proceed(request)
        }
    }
}
