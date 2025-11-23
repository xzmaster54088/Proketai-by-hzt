package com.pocketdev.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AiResponse(
    @Json(name = "explanation")
    val explanation: String,
    
    @Json(name = "actions")
    val actions: List<CodeAction>
)

@JsonClass(generateAdapter = true)
data class CodeAction(
    @Json(name = "fileName")
    val fileName: String,
    
    @Json(name = "codeContent")
    val codeContent: String,
    
    @Json(name = "commitMessage")
    val commitMessage: String
)
