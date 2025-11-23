package com.pocketdev.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketdev.data.remote.model.ChatCompletionRequest
import com.pocketdev.data.remote.model.ChatMessage
import com.pocketdev.data.remote.service.LlmApiService
import com.pocketdev.data.repository.UserSettingsRepository
import com.pocketdev.domain.model.AiResponse
import com.pocketdev.domain.model.CodeAction
import com.pocketdev.domain.model.SYSTEM_PROMPT
import com.pocketdev.domain.usecase.CommitFileUseCase
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val llmApiService: LlmApiService,
    private val userSettingsRepository: UserSettingsRepository,
    private val commitFileUseCase: CommitFileUseCase,
    private val moshi: Moshi
) : ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    fun sendMessage(userMessage: String) {
        viewModelScope.launch {
            // Add user message to chat
            val userChatMessage = ChatMessage("user", userMessage)
            _chatState.value = _chatState.value.copy(
                messages = _chatState.value.messages + userChatMessage,
                isLoading = true
            )

            try {
                // Get current model configuration
                val config = userSettingsRepository.configFlow.first()
                
                // Prepare messages with system prompt
                val messages = listOf(
                    ChatMessage("system", SYSTEM_PROMPT)
                ) + _chatState.value.messages + userChatMessage

                // Call LLM API
                val request = ChatCompletionRequest(
                    model = config.modelName,
                    messages = messages,
                    temperature = 0.7
                )

                val response = llmApiService.createChatCompletion(request)
                
                // Parse AI response
                val aiMessage = response.choices.firstOrNull()?.message?.content
                if (aiMessage != null) {
                    // Try to parse as JSON
                    val aiResponse = parseAiResponse(aiMessage)
                    
                    // Add AI message to chat
                    val aiChatMessage = ChatMessage("assistant", aiResponse.explanation)
                    _chatState.value = _chatState.value.copy(
                        messages = _chatState.value.messages + aiChatMessage,
                        currentAiResponse = aiResponse,
                        isLoading = false
                    )
                } else {
                    // Fallback if parsing fails
                    val fallbackMessage = ChatMessage("assistant", "I apologize, but I encountered an error processing your request.")
                    _chatState.value = _chatState.value.copy(
                        messages = _chatState.value.messages + fallbackMessage,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                // Handle errors
                val errorMessage = ChatMessage("assistant", "Error: ${e.message}")
                _chatState.value = _chatState.value.copy(
                    messages = _chatState.value.messages + errorMessage,
                    isLoading = false
                )
            }
        }
    }

    fun pushToGitHub(action: CodeAction) {
        viewModelScope.launch {
            // TODO: Get GitHub credentials from settings or auth manager
            // For now, using placeholder values
            val owner = "your-username"
            val repo = "your-repo"
            val token = "your-token" // This should come from GithubAuthManager
            
            val result = commitFileUseCase(
                owner = owner,
                repo = repo,
                path = action.fileName,
                content = action.codeContent,
                commitMessage = action.commitMessage,
                token = token
            )
            
            // Handle result (show success/failure message)
            result.onSuccess {
                // Show success message
                val successMessage = ChatMessage("system", "✅ Successfully pushed ${action.fileName} to GitHub")
                _chatState.value = _chatState.value.copy(
                    messages = _chatState.value.messages + successMessage
                )
            }.onFailure { error ->
                // Show error message
                val errorMessage = ChatMessage("system", "❌ Failed to push ${action.fileName}: ${error.message}")
                _chatState.value = _chatState.value.copy(
                    messages = _chatState.value.messages + errorMessage
                )
            }
        }
    }

    private fun parseAiResponse(jsonString: String): AiResponse {
        return try {
            val adapter = moshi.adapter(AiResponse::class.java)
            adapter.fromJson(jsonString) ?: throw JsonDataException("Failed to parse AI response")
        } catch (e: Exception) {
            // If JSON parsing fails, create a fallback response
            AiResponse(
                explanation = "I generated some code, but there was an issue with the response format. Here's what I was trying to do:\n\n$jsonString",
                actions = emptyList()
            )
        }
    }
}

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val currentAiResponse: AiResponse? = null
)
