package com.example.vynl_app.data.account

data class UserAccount(
    val userId: String,
    val displayName: String,
    val email: String
)

// Typed (not English text) so the UI can show a translated message.
enum class AccountError { BLANK_NAME, NAME_TOO_LONG, INVALID_EMAIL }

class AccountException(val error: AccountError) : Exception(error.name)
