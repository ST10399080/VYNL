package com.example.vynl_app.data.account

import android.util.Patterns
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

const val MAX_DISPLAY_NAME_LENGTH = 30

// Local stand-in until real auth lands. Swap for the Firebase-backed repository later;
// the SettingsViewModel only knows the interface.
class FakeAccountRepository(userId: String = "demo-user") : AccountRepository {
    private val state = MutableStateFlow(UserAccount(userId = userId, displayName = "You", email = ""))
    override val account: StateFlow<UserAccount> = state.asStateFlow()

    override suspend fun updateAccount(displayName: String, email: String): Result<Unit> {
        val name = displayName.trim()
        val mail = email.trim()
        val error = when {
            name.isEmpty() -> AccountError.BLANK_NAME
            name.length > MAX_DISPLAY_NAME_LENGTH -> AccountError.NAME_TOO_LONG
            // Email is optional until accounts exist; if given it must look like an address.
            mail.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(mail).matches() -> AccountError.INVALID_EMAIL
            else -> null
        }
        if (error != null) return Result.failure(AccountException(error))
        state.value = state.value.copy(displayName = name, email = mail)
        return Result.success(Unit)
    }
}
