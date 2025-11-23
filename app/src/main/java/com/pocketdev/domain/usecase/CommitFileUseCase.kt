package com.pocketdev.domain.usecase

import com.pocketdev.data.remote.model.CommitRequest
import com.pocketdev.data.remote.service.GitHubApi
import java.util.Base64
import javax.inject.Inject

class CommitFileUseCase @Inject constructor(
    private val gitHubApi: GitHubApi
) {
    
    suspend operator fun invoke(
        owner: String,
        repo: String,
        path: String,
        content: String,
        commitMessage: String,
        token: String
    ): Result<Unit> {
        return try {
            // Get existing file to get SHA (if exists)
            val existingFile = try {
                gitHubApi.getFileContent(owner, repo, path)
            } catch (e: Exception) {
                // File doesn't exist, that's fine - we'll create it
                null
            }
            
            // Encode content to Base64
            val encodedContent = Base64.getEncoder().encodeToString(content.toByteArray())
            
            // Prepare commit request
            val commitRequest = CommitRequest(
                message = commitMessage,
                content = encodedContent,
                sha = existingFile?.sha
            )
            
            // Update or create file
            gitHubApi.updateFile(owner, repo, path, commitRequest)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
