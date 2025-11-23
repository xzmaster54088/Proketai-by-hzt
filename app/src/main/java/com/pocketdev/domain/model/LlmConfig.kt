package com.pocketdev.domain.model

data class LlmConfig(
    val baseUrl: String,
    val apiKey: String,
    val modelName: String
) {
    fun isValid(): Boolean {
        return baseUrl.isNotBlank() && apiKey.isNotBlank() && modelName.isNotBlank()
    }
}
