package com.pocketdev.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChatCompletionRequest(
    @Json(name = "model")
    val model: String,
    
    @Json(name = "messages")
    val messages: List<ChatMessage>,
    
    @Json(name = "temperature")
    val temperature: Double = 0.7,
    
    @Json(name = "max_tokens")
    val maxTokens: Int? = null,
    
    @Json(name = "stream")
    val stream: Boolean = false
)

@JsonClass(generateAdapter = true)
data class ChatMessage(
    @Json(name = "role")
    val role: String, // "system", "user", "assistant"
    
    @Json(name = "content")
    val content: String
)
