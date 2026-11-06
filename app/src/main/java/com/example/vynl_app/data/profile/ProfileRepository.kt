package com.example.vynl_app.data.profile

import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun observeProfile(userId: String): Flow<UserProfile>
    suspend fun updateProfile(profile: UserProfile)
}
