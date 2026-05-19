package com.example.cheems_tour

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import java.util.Locale


class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val lang = getSharedPreferences("settings", MODE_PRIVATE)
            .getString("lang", "en") ?: "en"
        applyLocale(this, lang)
    }
}

fun applyLocale(context: Context, langCode: String) {
    val locale = Locale(langCode)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}