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

//•	Crea un objeto Locale con el langCode recibido.
//•	Llama a Locale.setDefault(locale) para que las operaciones de formato (fechas, números) usen el nuevo idioma.
//•	Crea un objeto Configuration basado en la config actual de los recursos.
//•	Aplica el Locale al Configuration con config.setLocale(locale).
//•	Llama a context.resources.updateConfiguration(config, displayMetrics)
// para que los recursos de strings (values/strings.xml vs values-es-rMX/strings.xml)
// sean resueltos con el nuevo idioma.
// parametro: context: Context — contexto Android; langCode: String — código ISO 639-1 ('en' o 'es').

fun applyLocale(context: Context, langCode: String)  {
    val locale = Locale(langCode)
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}