package com.pocketdev.data.remote.service

import com.pocketdev.data.remote.model.CommitRequest
import com.pocketdev.data.remote.model.CommitResponse
import com.pocketdev.data.remote.model.GitHubFileResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PUT
import retrofit2.http.Path

interface GitHubApi {
    
    @GET("repos/{owner}/{repo}/contents/{path}")
    suspend fun getFileContent(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String
    ): GitHubFileResponse

    @PUT("repos/{owner}/{repo}/contents/{path}")
    @Headers("Accept: application/vnd.github.v3+json")
    suspend fun updateFile(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("path") path: String,
        @Body body: CommitRequest
    ): CommitResponse
}
