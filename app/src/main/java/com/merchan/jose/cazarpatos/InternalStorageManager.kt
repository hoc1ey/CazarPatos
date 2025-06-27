package com.merchan.jose.cazarpatos

import android.content.Context
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

// Define a constant for the filename
private const val INTERNAL_FILENAME = "app_user_data.txt"
private const val DATA_SEPARATOR = "::DATA_SEPARATOR::"

class InternalStorageManager(private val context: Context) : FileHandler {

    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        val email = datosAGrabar.first
        val password = datosAGrabar.second

        val dataString = "$email$DATA_SEPARATOR$password"

        try {
            val outputStreamWriter = OutputStreamWriter(context.openFileOutput(INTERNAL_FILENAME, Context.MODE_PRIVATE))
            outputStreamWriter.write(dataString)
            outputStreamWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun ReadInformation(): Pair<String, String> {
        var email = ""
        var password = ""

        try {
            val inputStream = context.openFileInput(INTERNAL_FILENAME)

            if (inputStream != null) {
                val inputStreamReader = InputStreamReader(inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)
                val stringBuilder = StringBuilder()
                var receiveString: String?

                while (bufferedReader.readLine().also { receiveString = it } != null) {
                    stringBuilder.append(receiveString)
                }
                inputStream.close()

                val readData = stringBuilder.toString()
                val parts = readData.split(DATA_SEPARATOR)

                if (parts.size == 2) {
                    email = parts[0]
                    password = parts[1]
                } else {
                    System.err.println("InternalStorageManager: Data format error in file $INTERNAL_FILENAME")
                }
            }
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
        }

        return Pair(email, password)
    }

    fun deleteInternalFile(): Boolean {
        return context.deleteFile(INTERNAL_FILENAME)
    }
}