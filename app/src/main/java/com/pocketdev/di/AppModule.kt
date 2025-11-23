package com.pocketdev.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    // This module will be extended with more dependencies as we progress
    // For now, it serves as the base Hilt module
    
    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        return "https://api.openai.com" // Default base URL
    }
}
