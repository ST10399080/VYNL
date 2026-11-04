package com.example.vynl_app.ui.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

// Languages the app ships translations for. Must match res/xml/locales_config.xml and the
// values-<tag>/ resource folders. Native names are intentionally NOT translated.
enum class AppLanguage(val tag: String, val nativeName: String) {
    ENGLISH("en", "English"),
    ISIZULU("zu", "isiZulu"),
    FRENCH("fr", "Français"),
    SPANISH("es", "Español"),
    GERMAN("de", "Deutsch"),
    PORTUGUESE("pt", "Português")
}

object LanguageManager {
    fun current(): AppLanguage {
        val tag = AppCompatDelegate.getApplicationLocales().toLanguageTags()
            .ifBlank { Locale.getDefault().language }
        return AppLanguage.entries.firstOrNull { tag.startsWith(it.tag) } ?: AppLanguage.ENGLISH
    }

    // Recreates the activity; ViewModels and nav state survive that.
    fun set(language: AppLanguage) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language.tag))
    }
}
