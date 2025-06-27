package com.merchan.jose.cazarpatos

import android.app.Activity
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.IOException
import java.security.GeneralSecurityException

private const val PREFERENCE_FILE_NAME = "secret_shared_prefs"


class EncriptedSharedPreferencesManager(private val actividad: Activity) : FileHandler {
    private val masterKey: MasterKey = MasterKey.Builder(actividad.applicationContext)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences?

    init {
        sharedPreferences = try {
            EncryptedSharedPreferences.create(
                actividad.applicationContext,
                PREFERENCE_FILE_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        sharedPreferences?.edit()?.apply { putString(LOGIN_KEY, datosAGrabar.first)
            putString(PASSWORD_KEY, datosAGrabar.second)
            apply()
        }
    }

    override fun ReadInformation(): Pair<String, String> {
        val email = sharedPreferences?.getString(LOGIN_KEY, "") ?: ""
        val clave = sharedPreferences?.getString(PASSWORD_KEY, "") ?: ""
        return Pair(email, clave)
    }
}