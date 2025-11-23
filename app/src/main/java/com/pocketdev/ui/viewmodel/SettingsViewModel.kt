package com.pocketdev.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pocketdev.data.repository.UserSettingsRepository
import com.pocketdev.domain.model.LlmConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : ViewModel() {

    val configFlow: StateFlow<LlmConfig> = userSettingsRepository.configFlow

    fun updateConfig(baseUrl: String, apiKey: String, modelName: String) {
        viewModelScope.launch {
            userSettingsRepository.updateConfig(
                LlmConfig(
                    baseUrl = baseUrl,
                    apiKey = apiKey,
                    modelName = modelName
                )
            )
        }
    }

    fun updateBaseUrl(baseUrl: String) {
        viewModelScope.launch {
            userSettingsRepository.updateBaseUrl(baseUrl)
        }
    }

    fun updateApiKey(apiKey: String) {
        viewModelScope.launch {
            userSettingsRepository.updateApiKey(apiKey)
        }
    }

    fun updateModelName(modelName: String) {
        viewModelScope.launch {
            userSettingsRepository.updateModelName(modelName)
        }
    }
}
