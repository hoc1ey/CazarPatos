package com.merchan.jose.cazarpatos // Ensure this matches your package name

import android.content.Context
import android.os.Environment
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private const val EXTERNAL_FILENAME = "app_user_data_external.txt"
private const val DATA_SEPARATOR = "::DATA_SEPARATOR::"

class ExternalStorageManager(private val context: Context) : FileHandler {

    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }


    private fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }


    private fun getAppSpecificExternalDir(): File? {
        return context.getExternalFilesDir(null)
    }

    override fun SaveInformation(datosAGrabar: Pair<String, String>) {
        if (!isExternalStorageWritable()) {
            System.err.println("ExternalStorageManager: External storage not writable.")
            return
        }

        val appSpecificDir = getAppSpecificExternalDir()
        if (appSpecificDir == null) {
            System.err.println("ExternalStorageManager: Could not get app-specific external directory.")
            return
        }

        val file = File(appSpecificDir, EXTERNAL_FILENAME)
        val email = datosAGrabar.first
        val password = datosAGrabar.second
        val dataString = "$email$DATA_SEPARATOR$password"

        try {
            val fileOutputStream = FileOutputStream(file)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            outputStreamWriter.write(dataString)
            outputStreamWriter.close()
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle I/O errors
        }
    }

    override fun ReadInformation(): Pair<String, String> {
        if (!isExternalStorageReadable()) {
            System.err.println("ExternalStorageManager: External storage not readable.")
            return "" to ""
        }

        val appSpecificDir = getAppSpecificExternalDir()
        if (appSpecificDir == null) {
            System.err.println("ExternalStorageManager: Could not get app-specific external directory for reading.")
            return "" to ""
        }

        val file = File(appSpecificDir, EXTERNAL_FILENAME)
        var email = ""
        var password = ""

        if (!file.exists()) {
            return "" to ""
        }

        try {
            val fileInputStream = FileInputStream(file)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var receiveString: String?

            while (bufferedReader.readLine().also { receiveString = it } != null) {
                stringBuilder.append(receiveString)
            }
            fileInputStream.close()

            val readData = stringBuilder.toString()
            val parts = readData.split(DATA_SEPARATOR)

            if (parts.size == 2) {
                email = parts[0]
                password = parts[1]
            } else {
                System.err.println("ExternalStorageManager: Data format error in file $EXTERNAL_FILENAME")
            }
        } catch (e: FileNotFoundException) {
            System.err.println("ExternalStorageManager: File not found during read attempt: $EXTERNAL_FILENAME")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Pair(email, password)
    }

    fun deleteExternalFile(): Boolean {
        if (!isExternalStorageWritable()) {
            System.err.println("ExternalStorageManager: External storage not writable for delete.")
            return false
        }
        val appSpecificDir = getAppSpecificExternalDir()
        if (appSpecificDir == null) {
            System.err.println("ExternalStorageManager: Could not get app-specific external directory for delete.")
            return false
        }
        val file = File(appSpecificDir, EXTERNAL_FILENAME)
        return file.delete()
    }
}