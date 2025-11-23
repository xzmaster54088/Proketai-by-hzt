package com.pocketdev.data.remote.service

import com.pocketdev.data.remote.model.ChatCompletionRequest
import com.pocketdev.data.remote.model.ChatCompletionResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LlmApiService {
    
    /**
     * Standard OpenAI-compatible chat completion endpoint
     * The actual base URL will be dynamically replaced by DynamicHostInterceptor
     * This allows support for DeepSeek, OpenAI, and local Ollama
     */
    @POST("v1/chat/completions")
    suspend fun createChatCompletion(
        @Body request: ChatCompletionRequest
    ): ChatCompletionResponse
}
