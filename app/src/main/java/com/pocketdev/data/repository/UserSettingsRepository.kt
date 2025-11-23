package com.pocketdev.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.pocketdev.domain.model.LlmConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserSettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val BASE_URL_KEY = stringPreferencesKey("base_url")
        private val API_KEY_KEY = stringPreferencesKey("api_key")
        private val MODEL_NAME_KEY = stringPreferencesKey("model_name")
    }

    val configFlow: Flow<LlmConfig> = dataStore.data.map { preferences ->
        LlmConfig(
            baseUrl = preferences[BASE_URL_KEY] ?: "https://api.openai.com",
            apiKey = preferences[API_KEY_KEY] ?: "",
            modelName = preferences[MODEL_NAME_KEY] ?: "gpt-3.5-turbo"
        )
    }

    suspend fun updateBaseUrl(baseUrl: String) {
        dataStore.edit { preferences ->
            preferences[BASE_URL_KEY] = baseUrl
        }
    }

    suspend fun updateApiKey(apiKey: String) {
        dataStore.edit { preferences ->
            preferences[API_KEY_KEY] = apiKey
        }
    }

    suspend fun updateModelName(modelName: String) {
        dataStore.edit { preferences ->
            preferences[MODEL_NAME_KEY] = modelName
        }
    }

    suspend fun updateConfig(config: LlmConfig) {
        dataStore.edit { preferences ->
            preferences[BASE_URL_KEY] = config.baseUrl
            preferences[API_KEY_KEY] = config.apiKey
            preferences[MODEL_NAME_KEY] = config.modelName
        }
    }
}
