package com.pocketdev.data.remote.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommitRequest(
    val message: String,
    val content: String, // Base64 encoded
    val sha: String? = null // Null for new files
)

@JsonClass(generateAdapter = true)
data class CommitResponse(
    val content: GitHubFileContent?,
    val commit: CommitInfo
)

@JsonClass(generateAdapter = true)
data class GitHubFileResponse(
    val sha: String?,
    val content: String?,
    val encoding: String?
)

@JsonClass(generateAdapter = true)
data class GitHubFileContent(
    val name: String,
    val path: String,
    val sha: String,
    val content: String,
    val encoding: String
)

@JsonClass(generateAdapter = true)
data class CommitInfo(
    val sha: String,
    val message: String,
    val author: AuthorInfo?,
    val committer: AuthorInfo?
)

@JsonClass(generateAdapter = true)
data class AuthorInfo(
    val name: String,
    val email: String
)
